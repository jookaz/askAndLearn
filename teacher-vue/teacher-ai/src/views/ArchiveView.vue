<template>
  <div class="archive-container">
    <el-card v-if="currentQuestion">
      <template #header>
        <div class="header-container">
          <div class="question-content" :style="{ width: isEditing ? '800px' : 'auto' }">
            <el-input
              v-if="isEditing"
              v-model="currentQuestion.questionContent"
              type="textarea"
              :rows="4"
              :min-rows="4"
              style="width: 800px; min-height: 120px"
            />
            <span v-else>
              {{ currentQuestion.questionContent }}
            </span>
          </div>

          <div class="action-buttons">
            <el-button 
              @click="isEditing = !isEditing"
              :type="isEditing ? 'success' : 'primary'"
            >
              {{ isEditing ? '完成编辑' : '编辑问题' }}
            </el-button>
            <el-button 
              type="primary" 
              @click="showNewAnswerForm = true"
            >
              新建回答
            </el-button>
          </div>
        </div>
        <div v-if="showNewAnswerForm" class="new-answer-section">
          <el-input
            v-model="newAnswerContent"
            type="textarea"
            :rows="3"
            placeholder="请输入新的回答内容"
          />
          <el-button 
            type="primary" 
            class="confirm-btn"
            @click="handleArchiveAnswer"
          >
            确认归档
          </el-button>
        </div>
      </template>
      <!-- 修改第一个答案项 -->
      <div class="answer-item" v-if="answers[0]">
        <div class="answer-content">
          <el-input
            v-if="answerEditStates[0].editing"
            v-model="answerEditStates[0].content"
            type="textarea"
            :rows="2"
          />
          <span v-else>{{ answerEditStates[0].content }}</span>
        </div>
        <div class="answer-actions">
          <el-button
            @click="toggleAnswerEdit(0)"
            :type="answerEditStates[0].editing ? 'success' : 'primary'"
          >
            {{ answerEditStates[0].editing ? '完成编辑' : '编辑答案' }}
          </el-button>
          <el-button 
          type="primary" 
          @click="handleArchiveExistingAnswer(0)"
        >
          确认归档
        </el-button>
        </div>
      </div>
      

      <div class="answer-item" v-if="answers[1]">
        <div class="answer-content">
          <el-input
            v-if="answerEditStates[1].editing"
            v-model="answerEditStates[1].content"
            type="textarea"
            :rows="2"
          />
          <span v-else>{{ answerEditStates[1].content }}</span>
        </div>
        <div class="answer-actions">
          <el-button
            @click="toggleAnswerEdit(1)"
            :type="answerEditStates[1].editing ? 'success' : 'primary'"
          >
            {{ answerEditStates[1].editing ? '完成编辑' : '编辑答案' }}
          </el-button>
          <el-button 
          type="primary" 
          @click="handleArchiveExistingAnswer(1)"
        >
          确认归档
        </el-button>
        </div>
      </div>
      <div class="answer-item" v-for="(answer, index) in answers.slice(2)" :key="index">
        <div class="answer-content">
          <el-input
            v-if="answerEditStates[index + 2].editing"
            v-model="answerEditStates[index + 2].content"
            type="textarea"
            :rows="2"
          />
          <span v-else>{{ answerEditStates[index + 2].content }}</span>
        </div>
        <div class="answer-actions">
          <el-button
            @click="toggleAnswerEdit(index + 2)"
            :type="answerEditStates[index + 2].editing ? 'success' : 'primary'"
          >
            {{ answerEditStates[index + 2].editing ? '完成编辑' : '编辑答案' }}
          </el-button>
          <el-button
            type="primary"
            @click="handleArchiveExistingAnswer(index + 2)"
          >
            确认归档
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { 
  getAllAnswer,
  createQuestion, // 新增接口引用
  createAnswer    // 新增接口引用
} from '@/api/teacher'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router' 

const router = useRouter() // 在setup顶部添加
const isEditing = ref(false)
const newAnswerContent = ref('')
const showNewAnswerForm = ref(false)

const handleArchiveAnswer = async () => {
  if (!newAnswerContent.value) {
    ElMessage.error('请输入回答内容')
    return
  }

  try {
    // 归档问题
    const questionRes=await createQuestion({
      content: currentQuestion.value.questionContent,
      courseId: currentQuestion.value.courseId,
      chapterId: currentQuestion.value.chapterId,
      originalQuestionId: route.query.questionId
    })

    // 归档答案
    await createAnswer({
      content: newAnswerContent.value,
      questionId: questionRes.data.data
    })

    ElMessage.success('归档成功')
    newAnswerContent.value = ''
    showNewAnswerForm.value = false
    router.push('/teacher/questions') // 新增路由跳转
  } catch (error) {
    ElMessage.error('归档失败')
  }
}
// 新增归档方法
const handleArchiveExistingAnswer = async (index) => {
  const answerContent = answerEditStates.value[index].content
  
  if (!answerContent) {
    ElMessage.error('答案内容不能为空')
    return
  }

  try {
    // 归档问题
    const questionRes = await createQuestion({
      content: currentQuestion.value.questionContent,
      courseId: currentQuestion.value.courseId,
      chapterId: currentQuestion.value.chapterId,
      originalQuestionId: route.query.questionId
    })

    // 归档当前答案
    await createAnswer({
      content: answerContent,
      questionId: questionRes.data.data
    })

    ElMessage.success('归档成功')
    router.push('/teacher/questions')
  } catch (error) {
    ElMessage.error('归档失败')
  }
}
// 在获取问题数据时保存课程章节信息

const route = useRoute()
const currentQuestion = ref(null)
const answers = ref([])

// 新增答案编辑状态
const answerEditStates = ref([
  { editing: false, content: '' },
  { editing: false, content: '' }
])

// 初始化时填充内容
onMounted(async () => {
  if (!route.query.questionId) {
    ElMessage.error('缺少问题ID参数')
    return
  }
  

  try {
    const res = await getAllAnswer(route.query.questionId)
    const data = res.data.data
    
    currentQuestion.value = {
      questionContent: res.data.data.questionContent,
      courseId: res.data.data.courseId,
      chapterId: res.data.data.chapterId
    }
     // 先填充answers数组
    answers.value = [
      data.answerContent,
      data.improveAnswerContent,
      ...data.studentAnswerAllVOList.map(a => ({ content: a.answerContent }))
    ].filter(a => a)

    // 后初始化answerEditStates
    // 动态初始化所有答案的编辑状态
    // 修改初始化逻辑，移除未使用的idx参数
    answerEditStates.value = answers.value.map(ans => ({
      editing: false,
      content: typeof ans === 'string' ? ans : ans.content
    }));
    console.log(data.answerContent)
    console.log(data.improveAnswerContent)
    console.log(data.studentAnswerAllVOList)

    answers.value = [
      data.answerContent,
      data.improveAnswerContent,
      ...data.studentAnswerAllVOList.map(a => ({ content: a.answerContent }))
    ].filter(a => a)
  } catch (error) {
    ElMessage.error('数据加载失败')
  }
})

// 新增切换方法
// 修改切换方法
const toggleAnswerEdit = (index) => {
  const state = answerEditStates.value[index]
  state.editing = !state.editing
  
  if (!state.editing) {
    // 更新answers数组前先创建新数组保持响应式
    const newAnswers = [...answers.value]
    newAnswers[index] = state.content
    answers.value = newAnswers
  }
}
</script>

<style scoped>
.header-container {
  position: relative;
  min-height: 80px;
  padding-right: 220px; /* 预留按钮空间 */
}

.action-buttons {
  position: absolute;
  right: 20px;
  top: 20px;
  display: flex;
  flex-direction: row;
  gap: 10px;
  width: 200px; /* 固定按钮容器宽度 */
}

.problem-content {
  max-width: calc(100% - 220px); /* 限制内容区域宽度 */
  padding-right: 20px;
}

.answer-actions {
  display: flex;
  gap: 15px; /* 增加按钮间距 */
  margin-top: 10px;
}

/* 保持原有按钮基础样式 */
.el-button {
  margin-left: 0 !important;
  padding: 10px 15px;
  flex-shrink: 0; /* 防止按钮缩小 */
}

.form-card {
  margin-top: 20px;
}
.archive-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
}

.question-content {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.answer-item {
  margin: 15px 0;
  padding: 15px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #f8f9fa;
}
</style>
