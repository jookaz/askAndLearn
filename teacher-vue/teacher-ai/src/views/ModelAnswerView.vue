<template>
  <div class="model-answer-container">
    <el-table :data="questions" style="width: 100%" v-loading="loading" empty-text="暂无回答">
      <el-table-column prop="content" label="问题内容" width="300" />
      <el-table-column prop="answerContent" label="模范答案" />
    </el-table>

    <el-pagination
      :current-page="pagination.current"
      :page-size="pagination.size"
      :total="pagination.total"
      @current-change="handleCurrentChange"
      layout="prev, pager, next"
      class="pagination-wrapper"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useStore } from 'vuex'
import { getModelAnswers } from '@/api/student'

const store = useStore()
const loading = ref(false)
const questions = ref([])

const pagination = reactive({
  current: 1,
  size: 10,  // 保持前端变量名简洁
  total: 0
})

const fetchData = async () => {
  try {
    loading.value = true
    const { courseId, chapterId } = store.getters['chapter/getCurrentChapterParams']
    
    const res = await getModelAnswers({
      courseId,
      chapterId,
      pageNo: pagination.current,
      pageSize: pagination.size
    })

    if (res.data.code === 1) {
      questions.value = res.data.data.records
      pagination.total = res.data.data.total
    }
  } catch (error) {
    console.error('获取数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCurrentChange = (newPage) => {
  pagination.current = newPage
  fetchData()
}

onMounted(() => {
  if (store.getters['chapter/getCurrentChapterParams']?.courseId) {
    fetchData()
  }
})
</script>

<style scoped>
.model-answer-container {
  padding: 20px;
}

.pagination-wrapper {
  margin-top: 20px;
  justify-content: flex-end;
}

.el-table {
  margin-top: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}
</style>