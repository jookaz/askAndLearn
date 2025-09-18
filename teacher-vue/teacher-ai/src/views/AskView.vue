<template>
  <div class="ask-container">
    
    <el-card v-if="messages.length === 0" class="welcome-card">
      <div class="welcome-text">🎉 欢迎进入本章节的学习！！！</div>
      <div class="tip-text">请在下方的输入框中提出您的问题，我将为您提供专业的解答</div>
    </el-card>

    <!-- 对话历史区 -->
    <div ref="messageBox" class="message-list">
      <div v-for="(msg, index) in messages" :key="index" class="message-item">
        <!-- 用户提问 -->
        <div class="user-message">
          <el-icon class="avatar"><User /></el-icon>
          <div class="content-box">
            <div class="message-content">{{ msg.content }}</div>
          </div>
        </div>
        
        <!-- AI回复 -->
        <el-card class="ai-response">
          <div class="response-content" v-html="renderMarkdown(msg.response)"></div>
        </el-card>
      </div>
    </div>

    <!-- 输入区 -->
    <div class="input-area" v-if="!showMultiModelButton">
      <div class="input-wrapper">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题..."
          resize="none"
          @keyup.enter="sendMessage"
        />
        <el-button 
          type="primary" 
          :disabled="!canSend" 
          class="send-btn"
          @click="sendMessage">
          <el-icon class="send-icon"><Promotion /></el-icon>
          发送提问
        </el-button>
      </div>
    </div>

    <!-- 多模型回答区 -->
    <div class="multi-model-area" v-else>
      <el-button 
        type="success"
        class="multi-btn"
        @click="handleMultiModel">
        <el-icon><MagicStick /></el-icon>
        多模型深度解析
      </el-button>
      <div class="tip-text">
        <el-icon><InfoFilled /></el-icon>
        已为您匹配最佳解答方案，点击按钮查看多模型对比分析
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick } from 'vue'  // 修正导入项
// 在script setup部分添加
import { Promotion, MagicStick, InfoFilled } from '@element-plus/icons-vue'

// 在script setup部分添加
import { useRoute, useRouter } from 'vue-router'
import { updateProgressStatus } from '@/api/progress'

const router = useRouter()
import { ElMessage,ElMessageBox } from 'element-plus' // 新增消息提示组件
import { marked } from 'marked' // 新增markdown解析器
import hljs from 'highlight.js' // 新增代码高亮库
import { postChat } from '@/api/askyou' // 确保接口已导入

const route = useRoute()
const inputMessage = ref('')
const messages = reactive([])
const isChatting = ref(false)
const showMultiModelButton = ref(false)  // 新增响应式状态
const messageBox = ref(null)

const scrollToBottom = () => {
  if (messageBox.value) {
    messageBox.value.scrollTop = messageBox.value.scrollHeight
  }
}

const sendMessage = async () => {
  if (!canSend.value) return // 使用computed属性校验
  if (!inputMessage.value.trim()) return
  
  const question = inputMessage.value
  const msgId = Date.now()
  
  messages.push({
    id: msgId,
    content: question,
    response: '思考中...'
  })
  
  await nextTick()
  scrollToBottom()
  
  inputMessage.value = ''
  isChatting.value = true

  try {
    const res = await postChat({
      prompt: question,
      userId: Number(localStorage.getItem('student-id')),
      courseId: Number(route.params.courseId),
      chapterId: Number(route.params.chapterId)
    }, {
      timeout: 300000  // 新增超时配置（120秒）
    })
    
    if (res.data?.code === 1) {
      const msg = messages.find(m => m.id === msgId)
      if (msg) {
        // 修改响应处理逻辑
        const responseData = res.data.data || {}
        msg.response = responseData.response || ''
        msg.flag = responseData.flag  // 新增flag状态存储
        msg.question = responseData.question  // 存储问题
        msg.questionId = responseData.questionId  // 存储问题ID
        
        showMultiModelButton.value = msg.flag  // 修改为直接使用flag字段
        await nextTick()
        scrollToBottom()  // 响应后再次滚动
         // 在显示多模型按钮时更新进度（step 1）
        if (msg.flag) {
            updateProgressStatus({
                userId: Number(localStorage.getItem('student-id')),
                courseId: Number(route.params.courseId),
                chapterId: Number(route.params.chapterId),
                step: Number(1)
            })
        }
      }else{
        ElMessageBox.confirm(
          '大模型服务连接异常，错误信息: ' + (res.data?.message || '未知错误') + '，请刷新页面重试',
          '服务不可用',
          {
            confirmButtonText: '立即刷新',
            cancelButtonText: '取消',
            type: 'error'
          }
          ).then(() => {
            location.reload()
          })
      }
     
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  } finally {
    isChatting.value = false
  }
}


const canSend = computed(() => !isChatting.value)  // 保持计算属性定义

// Markdown配置（复用之前设置）
marked.setOptions({  // 确保marked已被定义
  highlight: code => hljs.highlightAuto(code).value,
  breaks: true
})
const renderMarkdown = content => marked(content || '')


// 修改后的handleMultiModel方法
const handleMultiModel = () => {
  const lastMsg = messages[messages.length - 1]
  router.push({
    name: 'chapter-detail',
    params: {
      courseId: route.params.courseId,
      chapterId: route.params.chapterId
    },
    query: {  // 改用query参数传递
      question: lastMsg.question,
      userId: Number(localStorage.getItem('student-id')),
      questionId: lastMsg.questionId
    }
  })
}
</script>

<style scoped>
/* 新增按钮样式 */
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

.welcome-card {
  margin-top: 30px;  /* 调整间距 */
}

.welcome-text {
  font-size: 24px;
  color: #409eff;
  margin-bottom: 15px;
  font-weight: 500;
}

.tip-text {
  color: #909399;
  font-size: 14px;
}

/* 优化欢迎卡片间距 */
.welcome-card {
  margin-bottom: 15px;  /* 原30px改为15px */
}

/* 调整输入区定位 */
.input-area {
  position: sticky;
  bottom: 0;
  background: white;
  z-index: 10;
  box-shadow: 0 -2px 12px rgba(0,0,0,0.05);
  padding: 15px 0;
}

/* 缩小消息列表高度 */
.message-list {
  height: 65vh;  /* 原70vh改为65vh */
  margin-bottom: 15px;
}

.input-wrapper {
  display: flex;
  gap: 15px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.send-btn {
  width: 120px;
  height: auto;
  display: flex;
  align-items: center;
  transition: all 0.3s;
}

.send-icon {
  margin-right: 8px;
  font-size: 16px;
}

/* 优化多模型区域 */
.multi-model-area {
  text-align: center;
  padding: 30px;
  background: #f8fafb;
  border-radius: 8px;
  margin-top: 20px;
}

.multi-btn {
  padding: 15px 40px;
  font-size: 16px;
  transition: transform 0.3s;
}

.multi-btn:hover {
  transform: scale(1.05);
}

.multi-btn .el-icon {
  margin-right: 10px;
  font-size: 18px;
}

.tip-text .el-icon {
  color: #409eff;
  margin-right: 8px;
}
/* 复用ChapterDetailView的样式 */
.ask-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 20px;
}

.input-area {
  position: sticky;
  bottom: 0;
  background: white;
  padding: 20px 0;
  box-shadow: 0 -4px 12px rgba(0,0,0,0.08);
  z-index: 10;
}

.ai-response {
  margin-top: 10px;
  background: #f8f9fa;
}

.message-list {
  height: 60vh;  
  margin-bottom: 10px; 
}

.input-area {
  position: relative; 
  margin-top: 0;     
  box-shadow: none;   
  padding: 10px 20px;  
}

.multi-model-area {
  text-align: center;
  padding: 20px;
}

.multi-btn {
  padding: 15px 40px;
}

.tip-text {
  color: #666;
  margin-top: 10px;
}
</style>
