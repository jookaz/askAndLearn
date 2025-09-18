
<template>
  <div class="question-detail">
          <el-button 
          type="primary" 
          @click="showScoreDialog = true"
          class="mb-4"
        >
          📝 评分标准
        </el-button>

        <!-- 对话框包裹现有评分表单 -->
        <el-dialog
          v-model="showScoreDialog"
          title="评分标准"
          width="720px"
        >
        <div class="markdown-container">
              <el-alert
                title="先判断回答相关性，再根据以下标准评分"
                type="info"
                :closable="false"
                class="mb-4"
              />

              <el-descriptions
                border
                :column="1"
                title="评分细则"
              >
                <el-descriptions-item label="80-100分 | 完美回答">
                  <span class="text-emerald-600 font-medium">✅ 完全准确、完整、逻辑严密</span>
                  <pre class="example">示例：
          问题：什么是光合作用
          回答："光合作用分为光反应和暗反应：
          1. 光反应中叶绿体吸收光能分解水
          2. 释放氧气并生成ATP和NADPH
          3. 暗反应通过卡尔文循环固定CO₂生成葡萄糖"</pre>
                </el-descriptions-item>

                <el-descriptions-item label="60-80分 | 优秀回答">
                  <span class="text-blue-600 font-medium">✏️ 正确但简略，逻辑清晰</span>
                  <pre class="example">示例：
          回答："光合作用是植物利用光能将CO₂和水
          转化为葡萄糖的过程，分为光反应和暗反应"</pre>
                </el-descriptions-item>

                <el-descriptions-item label="40-60分 | 基本正确">
                  <span class="text-orange-600 font-medium">⚠️ 存在小错误/信息不完整</span>
                  <pre class="example">示例：
          回答："光合作用是植物制造食物的过程
          需要阳光、水和二氧化碳，产生氧气"</pre>
                </el-descriptions-item>

                <el-descriptions-item label="20-40分 | 部分错误">
                  <span class="text-rose-600 font-medium">❌ 明显错误/偏离主题</span>
                  <pre class="example">示例：
          回答："光合作用就是植物吸收阳光
          然后产生能量供自己使用"</pre>
                </el-descriptions-item>

                <el-descriptions-item label="1-20分 | 完全错误">
                  <span class="text-red-700 font-medium">⛔ 无关/无法理解</span>
                  <pre class="example">示例：
          回答："光合作用是动物消化食物的方式"</pre>
                </el-descriptions-item>
              </el-descriptions>

     
            </div>
        </el-dialog>
    <el-card class="dialog-container">
      <!-- 学生问题 -->
      <div class="message student">
        <div class="avatar">👤</div>
        <div class="bubble">
          <h3>学生提问</h3>
          <p>{{ questionContent }}</p>
        </div>
      </div>

      <!-- 学生答案 -->
      <div class="message answer">
        <div class="avatar">📝</div>
        <div class="bubble">
          <h3>学生答案</h3>
          <pre>{{ answerContent }}</pre>
          
          <!-- 新增评分表单 -->
          <el-form 
            :model="scoreForm" 
            :rules="scoreRules"
            @submit.prevent="submitScore"
          >
            <el-form-item label="请输入评分（1-100）" prop="score">
              <el-input-number 
                v-model="scoreForm.score"
                :min="1"
                :max="100"
                :placeholder="scoreForm.submittedScore ? '已提交评分：' + scoreForm.submittedScore : '请输入评分'"
                controls-position="right"
              ></el-input-number>
            </el-form-item>
            
            <el-button 
              type="primary" 
              native-type="submit"
              :loading="submitting"
            >
              提交评分
            </el-button>
          </el-form>

          <!-- 新增对话面板 -->
          <el-collapse v-model="activeCollapse">
            <el-collapse-item name="chat" :disabled="!scoreSubmitted">
              <template #title>
                <span class="chat-title">💬 改进该学生的答案</span>
              </template>
              <div class="chat-container">
                <!-- 消息展示区 -->
                <div class="chat-messages">
                  <div 
                    v-for="(msg, index) in chatHistory" 
                    :key="index"
                    :class="['message', msg.role]"
                  >
                    <div class="role-icon">{{ msg.role === 'user' ? '👤' : '🤖' }}</div>
                    <div class="content">{{ msg.content }}</div>
                  </div>
                </div>
                
                <!-- 输入区 -->
                <div class="chat-input" v-if="!showCompletion">
                  <el-input 
                    v-model="userInput"
                    placeholder="输入您的回答..."
                    clearable
                    type="textarea"
                    :rows="3"
                    :autosize="{ minRows: 3, maxRows: 6 }"
                    @keyup.enter="handleKeydown"
                  />
                  <el-button 
                    type="primary" 
                    @click="handleSend"
                    :loading="isSending"
                    style="margin-top: 10px;"
                  >
                    发送
                  </el-button>
                </div>
                <!-- 修改完成提示结构 -->
                <template v-if="showReasonInput">
                  <el-alert
                    title="请说明回答理由"
                    type="warning"
                    :closable="false"
                    class="mb-4"
                  >
                    <el-form :model="reasonForm" :rules="reasonRules">
                      <el-form-item label="回答理由" prop="studentReason" class="reason-input">
                        <el-input
                          v-model="reasonForm.studentReason"
                          type="textarea"
                          :rows="4"
                          :autosize="{ minRows: 4, maxRows: 8 }"
                          placeholder="请详细说明您给出此回答的理由"
                          style="width: 600px;"
                        />
                      </el-form-item>
                      <el-button 
                        type="primary"
                        @click="submitReason"
                        :disabled="!reasonForm.studentReason.trim()"
                      >
                        提交理由
                      </el-button>
                    </el-form>
                  </el-alert>
                </template>
                <div v-if="showCompletion" class="completion-alert">
                    <el-alert
                      title="🎉 任务完成"
                      type="success"
                      description="恭喜您完成本章节的他问你答！可返回课程列表继续学习其他章节。"
                      :closable="false"
                    >
                      <template #extra>
                        <el-button 
                          type="primary" 
                          @click="$router.push({ name: 'courses' })"
                          class="back-btn"
                        >
                          返回课程列表
                        </el-button>
                      </template>
                    </el-alert>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive} from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/utils/request'
import { ElMessage,ElMessageBox} from 'element-plus'
import { updateProgressStatus } from '@/api/progress'  // 新增进度接口导入
import { useStore } from 'vuex'
const chatHistory = ref([]) // 新增chatHistory声明
const showScoreDialog = ref(false);

// 合并所有状态声明
const activeCollapse = ref([])
const scoreSubmitted = ref(false)
const userInput = ref('')
const isSending = ref(false)

const route = useRoute()
const questionContent = ref('')
const answerContent = ref('')
const answerId = ref(null)
const showReasonInput = ref(false) // 新增理由输入状态
const showCompletion = ref(false) // 保留原完成状态

// 新增理由表单数据
const reasonForm = reactive({
  studentReason: '',
  answerId: null
})
// 新增键盘事件处理方法
const handleKeydown = (e) => {
  if (e.shiftKey && e.key === 'Enter') {
    // Shift+Enter 允许换行
    return true
  } else if (e.key === 'Enter') {
    // 单独Enter触发发送
    e.preventDefault()
    handleSend()
  }
}
onMounted(async () => {
  try {
    const questionId = Number(route.params.questionId)
    const res = await axios.get('/student/question/detail', {
      params: { questionId }
    })
    questionContent.value = res.data.data.questionContent
    answerContent.value = res.data.data.answerContent
    answerId.value = res.data.data.answerId // 新增此行存储answerId
    console.log('获取到回答ID:', answerId.value) // 调试日志
  } catch (error) {
    console.error('获取问题详情失败:', error)
  }
})

// 修改后的响应式数据
const scoreForm = reactive({
  score: null,
  submittedScore: null // 新增已提交评分状态
})

// 修改后的提交方法
const submitScore = async () => {
  try {
    submitting.value = true
    const res = await axios.put('/student/studentAnswer', {
      answerId: answerId.value,
      anotherStudentScore: scoreForm.score
    })
    
    if (res.data?.code === 1) {
      ElMessage.success('评分提交成功')
      scoreForm.submittedScore = scoreForm.score // 存储已提交评分
      scoreForm.score = null // 清空当前输入值
      scoreSubmitted.value = true
      activeCollapse.value = ['chat']
    }
  } finally {
    submitting.value = false
  }
}

// 在模板中添加placeholder
const submitting = ref(false)
const scoreRules = {
  score: [
    { required: true, message: '请输入评分', trigger: 'blur' },
    { type: 'number', min: 1, max: 100, message: '评分范围1-100', trigger: ['blur', 'change'] }
  ]
}
const reasonRules = {
  studentReason: [
    { required: true, message: '必须输入回答理由', trigger: 'blur' },
    { min: 1, message: '理由不可以为空', trigger: 'blur' }
  ]
}



// 在setup部分获取store实例
const store = useStore()

// 修改后的接口参数获取方式
const handleSend = async () => {
  try {
    if (!userInput.value.trim()) return
    isSending.value = true
    
    // 添加用户消息
    chatHistory.value.push({
      role: 'user',
      content: userInput.value
    })
    
    const res = await axios.post('/student/someoneAsk/chat/improveAnswer', {
      prompt: userInput.value,
      userId: Number(localStorage.getItem('student-id')),
      courseId: store.getters['chapter/getCurrentChapterParams'].courseId,
      chapterId: store.getters['chapter/getCurrentChapterParams'].chapterId,
      questionId: Number(route.params.questionId),
      question:questionContent.value
    }, {
      timeout: 300000
    })
    if (res.data?.code === 1){
    // 添加AI响应
        
         chatHistory.value.push({
          role: 'assistant',
          content: res.data.data.response
        })

        // 新增flag判断逻辑
        if (res.data.data.flag) {
          reasonForm.answerId = res.data.data.id // 存储返回的answerId
          showReasonInput.value = true // 改为显示理由输入
          await axios.put('/student/question/updateIfSelect', null, {
            params: {
              questionId: Number(route.params.questionId)
            }
          })
          showCompletion.value = true
          
          // 修复参数获取方式
          await updateProgressStatus({
            userId: Number(localStorage.getItem('student-id')),  // 从路由参数获取
            courseId: store.getters['chapter/getCurrentChapterParams'].courseId,
            chapterId: store.getters['chapter/getCurrentChapterParams'].chapterId,
            step: 3
          })
          
          // 进度更新提示
          ElMessage.success('进度已更新')
        }
        
        userInput.value = ''
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
    
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  } finally {
    isSending.value = false
  }
}
const submitReason = async () => {
  try {
    const res = await axios.put('/student/studentImproveAnswer/reason', {
      answerId: reasonForm.answerId,
      studentReason: reasonForm.studentReason
    })
    
    if (res.data?.code === 1) {
      ElMessage.success('理由提交成功')
      showReasonInput.value = false
      showCompletion.value = true // 最后显示完成提示
    }
  } catch (error) {
    ElMessage.error('提交失败: ' + error.message)
  }
}

</script>

<style scoped>
.reason-input .el-textarea__inner {
  min-width: 600px;
  max-width: 800px;
}
.markdown-container {
  padding: 0 12px;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

:deep(.el-descriptions__title) {
  font-size: 16px;
  color: #409eff;
}

.example {
  background: #f8f9fa;
  padding: 12px;
  border-left: 4px solid #e5e7eb;
  margin: 8px 0;
  border-radius: 4px;
  color: #4b5563;
  white-space: pre-wrap;
  font-family: 'Courier New', monospace;
}
.dialog-container {
  max-width: 800px;
  margin: 20px auto;
}

.message {
  display: flex;
  margin: 20px 0;
}

.student {
  flex-direction: row;
}

.answer {
  flex-direction: row-reverse;
}

.avatar {
  font-size: 24px;
  margin: 0 15px;
}

.bubble {
  flex: 1;
  padding: 15px;
  border-radius: 12px;
  background: #f0f4ff;
  position: relative;
}

.bubble h3 {
  color: #409eff;
  margin-bottom: 10px;
}

pre {
  white-space: pre-wrap;
  line-height: 1.6;
  background: rgba(255,255,255,0.8);
  padding: 10px;
  border-radius: 6px;
}

/* 新增评分表单样式 */
.el-form {
  margin-top: 20px;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.el-form-item {
  margin-bottom: 15px;
}

.el-input-number {
  width: 200px;
}

/* 新增对话样式 */
.chat-title {
  font-size: 16px;
  color: #409eff;
}

.chat-container {
  margin-top: 15px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 10px;
}

.chat-messages {
  min-height: 300px;
  max-height: 500px;
  overflow-y: auto;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 6px;
}

.message .content {
  white-space: pre-wrap;
  word-break: break-word;
  padding: 8px;
  line-height: 1.6;
}

.message.user {
  background: #f5f7fa;
}

.message.assistant {
  background: #f0f9eb;
}

.role-icon {
  font-size: 20px;
  margin-right: 10px;
}

.content {
  flex: 1;
  line-height: 1.6;
}
</style>

