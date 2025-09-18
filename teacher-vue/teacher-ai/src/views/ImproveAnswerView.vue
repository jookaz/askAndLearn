<template>
  <div class="improve-container">
    <el-card class="question-header">
      <div class="header-content">
        <h3 style="color:#409EFF">📝 问题：{{ route.query.question }}</h3>
      </div>
    </el-card>

    <!-- 对话区 -->
    <div class="chat-container">
      <div ref="messageBox" class="message-list">
        <div v-for="(msg, index) in messages" :key="index" class="message-item">
          <!-- 用户消息 -->
          <div class="user-message">
            <el-icon class="avatar"><User /></el-icon>
            <div class="content-box">
              <div class="message-content">
                {{ msg.content }}
              </div>
            </div>
          </div>

          <!-- AI响应 -->
          <div class="ai-response">
            <el-card class="response-card">
              <div class="response-content" v-html="renderMarkdown(msg.response)" />
            </el-card>
          </div>
        </div>
      </div>

      <!-- 输入区 -->
      <div v-if="showInput" class="input-area">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="4"
          placeholder="请输入您的见解"
          :disabled="isSubmitting"
          @keyup.enter.prevent="handleSubmit" 
        />
        <el-button
          type="primary"
          class="submit-btn"
          :loading="isSubmitting"
          @click="handleSubmit"
        >
          提交
        </el-button>
      </div>

      <!-- 完成提示 -->
      <div v-else class="completion-tip">
        <el-result icon="success" title="恭喜完成本章节学习！">
          <template #extra>
            <el-button type="primary" @click="$router.push({
              name: 'AnswerSomeone',
              params: {
                courseId: route.params.courseId,  // 参数传递
                chapterId: route.params.chapterId  // 参数传递
              }
            })">查看他人问题</el-button>
          </template>
        </el-result>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { marked } from 'marked'
import hljs from 'highlight.js'
import { chatAnswer } from '@/api/askyou'
import { updateProgressStatus } from '@/api/progress' // 新增此行

const route = useRoute()
const messages = reactive([])
const inputMessage = ref('')
const isSubmitting = ref(false)
const showInput = ref(true)
const messageBox = ref(null)

// 初始化Markdown配置
marked.setOptions({
  highlight: code => hljs.highlightAuto(code).value,
  breaks: true
})

const renderMarkdown = (content) => {
  return content ? marked(content) : ''
}

const handleSubmit = async () => {
  if (!inputMessage.value.trim()) return

  try {
    isSubmitting.value = true
    const userMsg = inputMessage.value
    inputMessage.value = ''

    // 添加用户消息
    messages.push({
      content: userMsg,
      response: '思考中...'
    })
    await nextTick()
    scrollToBottom()

    // 调用接口
    const res = await chatAnswer({
      prompt: userMsg,
      userId: Number(localStorage.getItem('student-id')),
      courseId: Number(route.params.courseId),
      chapterId: Number(route.params.chapterId),
      question: route.query.question,
      questionId: Number(route.query.questionId)
    })

    // 修改handleSubmit方法中的成功回调
    if (res.data?.code === 1) {
      // 修改为获取嵌套的response字段
      const responseData = res.data.data || {}
      const lastResponse = responseData.response || ''
      
      // 更新最后一条消息的响应
      messages[messages.length - 1].response = lastResponse
      
      // 修改判断逻辑为使用flag字段
      if (responseData.flag === true) {
          showInput.value = false
          
          // 在隐藏输入栏时更新进度（step 3）
          updateProgressStatus({
              userId: Number(localStorage.getItem('student-id')),
              courseId: Number(route.params.courseId),
              chapterId: Number(route.params.chapterId),
              step: 3
          })
      }
    }
  } catch (error) {
    ElMessage.error('提交失败：' + error.message)
  } finally {
    isSubmitting.value = false
    scrollToBottom()
  }
}

const scrollToBottom = () => {
  if (messageBox.value) {
    messageBox.value.scrollTop = messageBox.value.scrollHeight
  }
}
</script>

<style scoped>
.back-btn {
  margin-bottom: 20px;
  padding: 10px 20px;
  border-radius: 18px;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
  transition: transform 0.3s;
}

.back-btn:hover {
  transform: translateY(-2px);
}
/* 复用ChapterDetailView的样式 */
.improve-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.chat-container {
  margin-top: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background: #f8f9fa;
}

.message-list {
  height: 60vh;
  padding: 20px;
  overflow-y: auto;
  scroll-behavior: smooth;
}

.message-item {
  margin-bottom: 30px;
}

.user-message {
  display: flex;
  align-items: start;
  margin-bottom: 20px;
}

.avatar {
  font-size: 28px;
  margin-right: 15px;
  color: #409eff;
}

.content-box {
  flex: 1;
}

.message-content {
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.input-area {
  padding: 20px;
  background: #fff;
  border-top: 1px solid #ebeef5;
  display: flex;
  gap: 15px;
  flex-direction: column;
}

.submit-btn {
  width: 120px;
  margin-left: auto;
}

.completion-tip {
  padding: 20px;
  text-align: center;
}

/* 响应卡片样式 */
.ai-response {
  margin-top: 15px;
}

.response-card {
  background: #f6f8fa;
  border-radius: 8px;
}

.response-content {
  padding: 15px;
  line-height: 1.6;
}
</style>