<template>
  <div class="chapter-detail">

    <el-card class="model-selector">
      <div class="model-header">
        <h3 style="color: #409eff;margin: 0">🔮 多模型对比分析</h3>
      </div>
    <div class="model-selection-container">
      <div class="selection-tip">
        <el-icon><Warning /></el-icon>
        请选择至少1个AI模型进行对比分析（如果超过3分钟没有响应请重新刷新页面或者选择少量模型再试）
      </div>
     <!-- 多选按钮组 -->
      <el-checkbox-group 
        v-if="showModelSelector"
        v-model="selectedModels"
        class="model-checkbox-group"
        :disabled="isChatting">
        <el-checkbox-button
          v-for="model in availableModels"
          :key="model"
          :label="model"
          class="model-btn">
          {{ model }}
        </el-checkbox-button>
      </el-checkbox-group>
      
      <!-- 已选模型展示 -->
      <div v-else class="selected-models">
        <el-tag
          v-for="model in selectedModels"
          :key="model"
          type="info"
          class="model-tag"
        >
          <el-icon><MagicStick /></el-icon>
          {{ model }}
        </el-tag>
      </div>
    </div>
      <!-- 优化后的回答按钮 -->
      <div v-if="showAnswerButton" style="margin-top: 15px;">
        <el-button
          type="success"
          :disabled="!canSend"
          class="compact-btn"
          @click="handleAnswer"
        >
          <el-icon><MagicStick /></el-icon>
          开始分析
        </el-button>
      </div>
    </el-card>

   

    <!-- 对话区 -->
    <div class="chat-container">
      <!-- 消息展示区 -->
      <div
        ref="messageBox"
        class="message-list"
      >
        <div 
          v-for="(msg, index) in messages"
          :key="index"
          class="message-item"
        >
          <div class="user-message">
            <el-icon class="avatar">
              <User />
            </el-icon>
            <div class="content-box">
              <div class="message-content">
                {{ msg.content }}
              </div>
            </div>
          </div>
          
          <div class="ai-responses">
            <el-card
              v-for="model in selectedModels"
              :key="model"
              class="response-card"
            >
              <template #header>
                <div class="model-name">
                  {{ model }}
                </div>
              </template>
              <div class="response-content">
                <div v-html="renderMarkdown(getModelResponse(msg.id, model))" />
                <!-- 优化后的评分组件 -->
                <div class="rating-wrapper" v-if="msg.hasAllResponses">
                  <div class="rating-tip">请对模型回答进行评分（1-100分）：</div>
                  <el-input-number 
                    v-model="msg.scores[model]"
                    :min="1"
                    :max="100"
                    :step="1"
                    size="small"
                    @change="(val) => handleScoreChange(msg.id, model, val)"
                    class="number-rating"
                  />
                  <!-- 新增理由输入 -->
                  <div v-if="msg.scores[model] !== null" class="reason-input">
                    <el-input
                      v-model="msg.reasons[model]"
                      type="textarea"
                      :rows="2"
                      placeholder="请输入评分理由"
                      class="mt-2"
                    />
                    <el-button
                      type="primary"
                      size="small"
                      class="mt-2"
                      :disabled="!msg.reasons[model]?.trim()"
                      @click="handleSubmitReason(msg.id, model)"
                    >
                      提交理由
                    </el-button>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </div>
    <div class="improve-section" v-if="scoreSubmitted">
      <el-card class="improve-card">
        <h4>💡 请输入你对该问题的见解</h4>
        
        <!-- 新增对话界面 -->
        <div class="chat-container">
          <div class="chat-messages">
            <div 
              v-for="(msg, index) in improveHistory"
              :key="index"
              :class="['message', msg.role]"
            >
              <div class="role-icon">{{ msg.role === 'user' ? '👤' : '🤖' }}</div>
              <div class="content" v-html="renderMarkdown(msg.content)" />
            </div>
          </div>
          
          <div class="chat-input" v-if="!hasCompleted">
            <v-md-editor
              v-model="improveInput"
              :disabled-menus="[]"
              height="350px"
              placeholder="请输入改进建议（支持Markdown语法）..."
              @keydown.enter="handleKeydown"
              class="md-editor-custom"
            />
            <el-button
              type="primary"
              @click="handleImproveSend"
              :loading="isImproving"
              style="margin-top: 10px;"
            >
              发送
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

   <div v-if="hasCompleted" class="completion-message">
      <el-alert title="任务完成" type="success" :closable="false">
        <template #default>
          <span>🎉 您已成功完成本课程所有问答环节！</span>
        </template>
      </el-alert>
    </div>
  </div>
</template>

<script setup>
// 合并导入声明
import { ref, reactive, onMounted, computed, nextTick} from 'vue'  // 添加inject导入
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAvailableModels, multiModelChat ,chatAnswer} from '@/api/askyou'
import { useRoute} from 'vue-router'
import { MagicStick } from '@element-plus/icons-vue'  // 移除Refresh图标导入
import { updateModelScore,updateStudentReason } from '@/api/askyou'  // 确保导入更新评分接口
import { updateProgressStatus } from '@/api/progress'
// ... existing imports ...
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

VMdEditor.use(githubTheme);

// 在setup语法块外添加：
const handleKeydown = (e) => {
  if (!e.shiftKey) {
    e.preventDefault();
    handleImproveSend();
  }
};
const isImproving = ref(false)
const scoreSubmitted = ref(false)



const route = useRoute()
const messages = reactive([])
const selectedModels = ref([])
const isChatting = ref(false)
const availableModels = ref([])

// 修改参数获取方式
const question = ref(route.query.question || '')
const questionId = ref(route.query.questionId || null)
const userId = ref(route.query.userId || null)

// 新增缺失的状态声明
const improveInput = ref('')
const improveHistory = ref([])
// 新增完成状态计算属性
const hasCompleted = computed(() => 
  improveHistory.value.some(msg => 
    msg.role === 'assistant' && 
    msg.originalData?.flag // 新增原始数据存储
  )
)

// 修改后的改进对话处理方法
const handleImproveSend = async () => {
  try {
    isImproving.value = true
    improveHistory.value.push({
      role: 'user',
      content: improveInput.value
    })

    const { data } = await chatAnswer({
      prompt: improveInput.value,
      userId: Number(userId.value),  // 从查询参数获取
      courseId: Number(route.params.courseId),  // 从路由参数获取
      chapterId: Number(route.params.chapterId),  // 从路由参数获取
      questionId: Number(questionId.value),  // 从路由参数获取
      question: question.value  // 从查询参数获取问题内容
    }, {
      timeout: 300000
    })

    // 添加AI响应
    // 修改接口响应处理
    improveHistory.value.push({
      role: 'assistant',
      content: data.data.response,
      originalData: data.data // 存储原始响应数据
    })

    // 修复问题一：无论是否成功都清空输入框
    improveInput.value = '' 

    // 修复问题二：正确使用flag字段判断
    if (data.data.flag) {
      await updateProgressStatus({
        userId: Number(route.query.userId),
        courseId: Number(route.params.courseId),
        chapterId: Number(route.params.chapterId),
        step: 2
      })
      
      // 显示完成提示
      ElMessageBox.alert('恭喜您已完成所有问答环节！', '任务完成', {
        confirmButtonText: '确定',
        type: 'success'
      })
      
      // 新增完成状态管理
      // scoreSubmitted.value = false
    }
  } catch (error) {
    ElMessage.error('请求失败: ' + error.message)
  } finally {
    isImproving.value = false
  }
}
// 修复初始化逻辑
onMounted(() => {
  fetchModels().then(() => {
    if (question.value) { // 直接检查question是否有值
      sendMessage()
    }
  })
})
// 修改后的评分处理
const handleScoreChange = async (msgId, modelName, score) => {
  try {
    const answerId = messages.find(m => m.id === msgId)?.modelAnswerIds?.[modelName]
    if (!answerId) return
    
    const res = await updateModelScore(answerId, score)
    if (res.data?.code === 1) {
      ElMessage.success('评分提交成功')
      const allRated = selectedModels.value.every(m => 
        messages.find(msg => msg.id === msgId)?.scores[m] !== null
      )
      if (allRated) {
        scoreSubmitted.value = true  // 保持原有逻辑触发改进组件
      }
    }
  } catch (error) {
    ElMessage.error('评分提交失败: ' + error.message)
  }
}
// 在setup中添加
const handleSubmitReason = async (msgId, model) => {
  const currentMsg = messages.find(m => m.id === msgId)
  if (!currentMsg?.modelAnswerIds?.[model] || !currentMsg.reasons[model]?.trim()) return

  try {
    const res = await updateStudentReason({
      id: currentMsg.modelAnswerIds[model],
      studentReason: currentMsg.reasons[model]
    })
    if (res.data?.code === 1) {
      ElMessage.success('理由提交成功')
    }
  } catch (error) {
    ElMessage.error('提交失败：' + error.message)
  }
}
// 新增状态声明
const showModelSelector = ref(true)
// const showAnswerButton = ref(true)



const sendMessage = async () => {  
  if (!canSend.value) return
  
  const msgId = Date.now().toString()
  // 修改消息数据结构
  // 在消息对象中新增计算属性
  messages.push({
    id: msgId,
    content: question.value,  // 直接使用路由参数
    responses: Object.fromEntries(selectedModels.value.map(m => [m, '等待响应中...'])),
    modelAnswerIds: {}, // 新增模型回答ID存储
    scores: Object.fromEntries(selectedModels.value.map(m => [m, null])),
    reasons: Object.fromEntries(selectedModels.value.map(m => [m, ''])),
    hasAllResponses: false,
    allRated: computed(() => selectedModels.value.every(m => messages.find(msg => msg.id === msgId)?.scores[m] !== null)), // 修复变量引用
    promptShown: false // 防止重复提示
  })
  
  await nextTick()
  scrollToBottom()
  
  isChatting.value = true

  try {
    const res = await multiModelChat(
      selectedModels.value,
      question.value,
      Number(userId.value), // 转换为数字
      Number(route.params.courseId),
      Number(route.params.chapterId),
      Number(questionId.value) // 转换为数字
    )
    
    if (res.data?.code === 1) {
      const currentMsg = messages.find(m => m.id === msgId)
      res.data.data?.forEach(item => {
        if (currentMsg && item.modelName && item.response) {
          currentMsg.responses[item.modelName] = item.response
          currentMsg.modelAnswerIds[item.modelName] = item.id // 新增回答ID存储
          currentMsg.hasAllResponses = selectedModels.value.every(m => 
            !currentMsg.responses[m].includes('等待响应中...')
          )
        }
      })
    } else {
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
    console.error('请求异常:', error)
    ElMessage.error(`请求失败: ${error.message}`)
  } finally {
    isChatting.value = false
    scrollToBottom()
  }
}

// 获取模型响应内容
const getModelResponse = (msgId, model) => {
  const msg = messages.find(m => m.id === msgId)
  return msg ? msg.responses[model] : ''
}

// 是否可以发送消息
const canSend = computed(() => {
  return selectedModels.value.length > 0 && !isChatting.value
})

// 初始化获取模型列表
// 修改接口调用部分
const fetchModels = async () => {
  try {
    const res = await getAvailableModels(route.params.courseId)  // 添加courseId参数
    availableModels.value = res.data.data || []
  } catch (error) {
    ElMessage.error('获取模型列表失败')
  }
}

// 新增滚动方法
const messageBox = ref(null)
const scrollToBottom = () => {
  if (messageBox.value) {
    messageBox.value.scrollTop = messageBox.value.scrollHeight
  }
}

// 新增Markdown解析依赖
import { marked } from 'marked'
import hljs from 'highlight.js'

// 初始化Markdown配置
marked.setOptions({
  highlight: (code) => hljs.highlightAuto(code).value,
  breaks: true,
  sanitize: true,  // 完全禁用HTML过滤
})

// Markdown渲染方法
const renderMarkdown = (content) => {
  return content ? marked(content) : '<div class="loading-text">等待响应中...</div>'
}



// 添加自动发送逻辑
onMounted(() => {
  if (route.query.question) {
    // 当有路由参数时自动发送
    sendMessage()
  }
})
// 新增响应式状态
const showAnswerButton = ref(true)

// 修改后的处理逻辑
const handleAnswer = async () => {
  try {
    await ElMessageBox.confirm(
      `已选择 ${selectedModels.value.length} 个模型进行分析，确定开始吗？`,
      '确认分析请求',
      {
        confirmButtonText: '开始分析',
        cancelButtonText: '重新选择',
        type: 'warning',
      }
    )
    showAnswerButton.value = false
    showModelSelector.value = false
    await sendMessage()
  } catch (error) {
    // 用户取消选择
  }
}



</script>


<style scoped>
/* 新增容器样式 */
.model-selection-container {
  background: #f8fafc;
  border-radius: 8px;
  padding: 15px;
  margin-top: 10px;
  border: 1px solid #e2e8f0;
}

.selection-tip {
  color: #64748b;
  font-size: 13px;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 优化按钮组间距 */
.model-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.model-btn {
  padding: 10px 18px;
  border-radius: 18px;
  border: 1px solid #e2e8f0;
  transition: all 0.2s;
}

.model-btn.is-checked {
  background: #409eff;
  color: white;
  border-color: #409eff;
  box-shadow: 0 2px 6px rgba(64, 158, 255, 0.2);
}

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
/* 紧凑按钮样式 */
.compact-btn {
  padding: 8px 20px;
  font-size: 14px;
  width: auto;
  margin: 0 auto;
  display: block;
}

/* 已选模型标签样式 */
.selected-models {
  margin-top: 15px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.model-tag {
  padding: 8px 12px;
  border-radius: 15px;
  background: #f0f9eb;
  border-color: #e1f3d8;
}
/* 仅保留CSS样式 */
/* 按钮悬停效果 */
.el-button--success {
  background: linear-gradient(135deg, #67c23a, #85ce61);
  border: none;
  transition: all 0.3s;
}

.el-button--success:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(103,194,58,0.3);
}

.el-button--success .el-icon {
  margin-right: 8px;
  font-size: 18px;
}
.chapter-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.model-selector {
  margin-bottom: 20px;
}

.model-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.chat-container {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background: #f8f9fa;
}

.message-list {
  height: 60vh;
  padding: 20px;
  overflow-y: auto;  /* 已存在 */
  scroll-behavior: smooth;  /* 新增平滑滚动效果 */
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

.ai-responses {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.response-card {
  transition: transform 0.3s;
}

.response-card:hover {
  transform: translateY(-3px);
}

.model-name {
  font-weight: bold;
  color: #409eff;
}

.response-content {
  min-height: 100px;
  line-height: 1.6;
}

.input-area {
  padding: 20px;
  background: #fff;
  border-top: 1px solid #ebeef5;
  display: flex;
  gap: 15px;
}

.send-btn {
  width: 120px;
  height: auto;
}

/* 新增Markdown样式 */
.response-content :deep(pre) {
  background: #f6f8fa;
  padding: 12px;
  border-radius: 4px;
  margin: 10px 0;
}

.response-content :deep(code) {
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 14px;
}

.response-content :deep(.hljs) {
  background: transparent;
}

.response-content :deep(strong) {
  color: #409eff;
}
/* 优化评分样式 */
.rating-tip {
  color: #909399;
  font-size: 12px;
  margin-bottom: 8px;
}

.number-rating {
  display: flex;
  gap: 6px;
}

:deep(.el-radio-button__inner) {
  padding: 8px 12px;
  border-radius: 4px;
  transition: all 0.2s;
}

:deep(.el-radio-button__orig-radio:checked + .el-radio-button__inner) {
  background: #409eff;
  border-color: #409eff;
  color: white;
}

.improve-card {
  margin-top: 20px;
  transition: all 0.3s;
}

.submit-improve {
  margin-top: 15px;
  width: 200px;
}


/* 新增对话样式 */
.improve-card .chat-container {
  margin-top: 15px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 10px;
}

.chat-messages {
  height: 300px;
  overflow-y: auto;
  margin-bottom: 10px;
}

.message {
  display: flex;
  margin: 10px 0;
  padding: 8px;
  border-radius: 6px;
}

.user {
  background: #f0f4ff;
}

.assistant {
  background: #f6ffed;
}

.role-icon {
  font-size: 20px;
  margin-right: 10px;
}

.chat-input {
  margin-top: 15px;
}

</style>
