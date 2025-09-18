<template>
  <div class="answer-someone-container">
    <el-card class="header-card">
      <h3>📚 章节问题列表</h3>
      <template v-if="questions.length">
        <el-table 
          :data="questions" 
          stripe 
          style="width: 100%"
          @row-click="handleQuestionClick"
        >
          <el-table-column 
            prop="questionContent" 
            label="问题内容"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            prop="createTime"
            label="提问时间"
            width="180"
          ></el-table-column>
        </el-table>
      </template>
      <div v-else class="empty-state">
        <el-empty description="当前章节还没有其他同学的问题和回答">
          <el-button 
            type="primary" 
            @click="handleSelectChapter"
            class="select-chapter-btn"
          >
            <i class="el-icon-folder-opened"></i>
            选择其他章节
          </el-button>
        </el-empty>
      </div>
    </el-card>
  </div>
</template>



<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

// 新增点击处理
const handleQuestionClick = (row) => {
  router.push({
    name: 'QuestionDetail',
    params: { questionId: row.id }  // 确保传递数值型ID
  })
}
import { ref, onMounted } from 'vue'
// import { useRoute } from 'vue-router'
import { listQuestions } from '@/api/course'
import { inject } from 'vue'
const appState = inject('appState')

// 修改setup部分
// const route = useRoute()
const questions = ref([])

onMounted(async () => {
  try {
    const res = await listQuestions({
      courseId: Number(appState.currentCourseId),
      chapterId: Number(appState.currentChapterId)
    })
    if (res.data?.code === 1) {
      questions.value = res.data.data || []
    }
  } catch (error) {
    console.error('获取问题列表失败:', error)
  }
})

// 新增章节选择方法
const handleSelectChapter = () => {
  router.push({ name: 'chapters' }) // 确保路由配置中有这个名称
}
</script>

<style scoped>
.empty-state {
  padding: 40px 0;
  text-align: center;
}

.select-chapter-btn {
  margin-top: 20px;
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
}

.el-icon-folder-opened {
  margin-right: 8px;
}

.answer-someone-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

:deep(.el-table__row) {
  cursor: pointer;
  transition: all 0.3s;
}

:deep(.el-table__row:hover) {
  background-color: #f5f7fa;
  transform: translateX(5px);
}
</style>