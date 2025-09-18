<template>
  <div class="qa-container">
    <el-card class="operation-card">
      <el-button 
        type="primary" 
        @click="fetchChatIds" 
        :loading="loadingChatIds"
      >
        <el-icon><Refresh /></el-icon>
        获取会话历史
      </el-button>
    </el-card>

    <el-card class="chat-list" v-if="chatIds.length > 0">
      <div class="chat-item" v-for="chatId in chatIds" :key="chatId">
        <el-button 
          type="text" 
          @click="fetchChatHistory(chatId)"
        >
          {{ chatId }}
        </el-button>
      </div>
    </el-card>

    <el-card class="history-card" v-if="currentChat">
      <div class="message-item" v-for="(msg, index) in messages" :key="index">
        <div class="message-header">
          <el-tag type="info" size="small">{{ msg.role }}</el-tag>
          <span class="timestamp">{{ formatTime(msg.timestamp) }}</span>
        </div>
        <div class="message-content">{{ msg.content }}</div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getChatIds, getChatHistory } from '@/api/askyou'

const chatIds = ref([])
const messages = ref([])
const currentChat = ref(null)
const loadingChatIds = ref(false)

const fetchChatIds = async () => {
  try {
    loadingChatIds.value = true
    const res = await getChatIds()
    chatIds.value = res.data || []
  } catch (error) {
    ElMessage.error('获取会话列表失败: ' + error.message)
  } finally {
    loadingChatIds.value = false
  }
}

const fetchChatHistory = async (chatId) => {
  try {
    currentChat.value = chatId
    const res = await getChatHistory(chatId)
    messages.value = (res.data || []).map(item => ({
      role: item.role,
      content: item.content,
      timestamp: new Date().toISOString() // 如果接口没有返回时间，使用当前时间
    }))
  } catch (error) {
    ElMessage.error('获取聊天记录失败: ' + error.message)
  }
}

const formatTime = (timestamp) => {
  return new Date(timestamp).toLocaleString()
}
</script>

<style scoped>
.qa-container {
  max-width: 800px;
  margin: 20px auto;
}

.operation-card {
  margin-bottom: 20px;
}

.chat-list {
  margin-bottom: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.chat-item {
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.history-card {
  margin-top: 20px;
}

.message-item {
  margin: 15px 0;
  padding: 10px;
  border-radius: 4px;
  background: #f8f9fa;
}

.message-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.timestamp {
  margin-left: 15px;
  color: #909399;
  font-size: 12px;
}

.message-content {
  white-space: pre-wrap;
  line-height: 1.6;
}
</style>