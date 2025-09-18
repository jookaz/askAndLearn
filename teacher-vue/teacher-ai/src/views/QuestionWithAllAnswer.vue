<template>
  <div class="answer-container">
    <el-card class="question-card">
      <h2>{{ questionData.questionContent }}</h2>
      <div class="meta-info">
        <span>提问学生：{{ questionData.studentName }}</span>
        <span style="margin:0 10px">|</span>
        <span>所在班级：{{ questionData.classes }}</span>
        <span style="margin:0 10px">|</span>
        <span>提问时间：{{ new Date(questionData.createTime).toLocaleString() }}</span>
      </div>
      <div class="like-box">
        <el-button @click.stop="toggleLike(questionData)">
          <el-icon :class="{ 'liked': questionData.isLiked }">
            <CustomThumbIcon v-if="!questionData.isLiked" />
            <CustomThumbIcon v-else color="#409eff" />
          </el-icon>
          <span class="count">{{ questionData.likeNumber }}</span>
        </el-button>
      </div>
    </el-card>

    <div class="answer-section">
      <div class="header-bar">
        <h3>所有回答</h3>
        <el-button type="primary" @click="showEditor = true">写回答</el-button>
      </div>
      <el-dialog v-model="showEditor" title="撰写回答" width="80%">
        <v-md-editor 
          v-model="newAnswer"
          height="400px"
          placeholder="请输入您的回答（支持Markdown语法）"
        />
        <template #footer>
          <el-button @click="showEditor = false">取消</el-button>
          <el-button type="primary" @click="submitAnswer">提交回答</el-button>
        </template>
      </el-dialog>
        <el-card 
          v-for="(answer, index) in allAnswers"
          :key="answer.id"
          class="answer-card"
          :class="{ 'official-answer': index === 0 }"
        >
          <div v-if="answer" class="answer-header">
            <el-tag :type="answer.type === '问题提出者回答' ? 'success' : answer.type === '改进回答' ? 'warning' : 'info'">
              {{ answer.type }}
            </el-tag>
            <span class="student-info">
              {{ answer.studentName }}（{{ answer.classes }}）
            </span>
            <span v-if="answer.time" class="time">
              {{ new Date(answer.time).toLocaleString() }}
            </span>
          </div>
          <div class="answer-content">
            <v-md-preview :text="answer.content" />
          </div>
          <div class="action-bar">
            <el-button @click.stop="toggleAnswerLike(answer)">
              <el-icon :class="{ 'liked': answer.isLiked }">
                <CustomThumbIcon v-if="!answer.isLiked" />
                <CustomThumbIcon v-else color="#409eff" />
              </el-icon>
              <span class="count">{{ answer.likes }}</span>
            </el-button>
          </div>
        </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
// 替换原有导入
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';

import {
  getQuestionAllAnswers,
  updateQuestionLike,
  updateStudentAnswerLike,
  updateStudentImproveAnswerLike,
  updateStudentAnswerAllLike,
  createStudentAnswerAll
} from '@/api/askyou'
import CustomThumbIcon from '@/components/CustomThumbIcon.vue'
const showEditor = ref(false)
const newAnswer = ref('')
// 添加显式使用声明（不会影响实际功能）

// 添加ElMessage导入
import { ElMessage } from 'element-plus';

VMdPreview.use(vuepressTheme, {
  extend(css) {
    css += '.vuepress-markdown-body { padding: 20px; }'
    return css
  }
});

// 原初始化代码
// VMdEditor.use(vuepressTheme);
// 添加防重复初始化判断
if (!VMdEditor._initiated) {
  console.log('初始化VMdEditor'); // 调试用，确保只初始化一次
  VMdEditor.use(vuepressTheme);
  VMdEditor._initiated = true;
}
// 新增数据加载方法
const allAnswers = ref([]) // 初始化为空数组而非undefined

const loadQuestionData = async () => {
  try {
    const res = await getQuestionAllAnswers(route.params.questionId);
    questionData.value = res.data.data;
    allAnswers.value = [
      {
        type: '问题提出者回答',
        id: questionData.value.answerId,
        content: questionData.value.answerContent,
        studentName: questionData.value.answerStudentName,  // 新增
        classes: questionData.value.answerStudentClasses,          // 新增
        likes: questionData.value.answerLikeNumber,
        time: questionData.value.answerSubmitTime
      },
      questionData.value.improveAnswerId ? {
        type: '改进回答',
        id: questionData.value.improveAnswerId,
        studentName: questionData.value.improveAnswerStudentName,  // 新增
        classes: questionData.value.improveAnswerStudentClasses,          // 新增
        content: questionData.value.improveAnswerContent,
        likes: questionData.value.improveAnswerLikeNumber,
        time: questionData.value.improveAnswerSubmitTime
      } : null,
      ...questionData.value.studentAnswerAllVOList.map(a => ({
        type: '其他学生回答',
        id: a.id,
        content: a.answerContent,
        studentName: a.studentName,  // 新增
        classes: a.classes,          // 新增
        likes: a.likeNumber,
        time: a.submitTime
      }))
    ].filter(Boolean).map(a => ({
      ...a,
      isLiked: false
    }))

   
  } catch (error) {
    console.error('获取问题详情失败:', error);
  }
};

// 修改onMounted调用
onMounted(async () => {
  await loadQuestionData();
});

// 修改submitAnswer方法
const submitAnswer = async () => {
  if (!newAnswer.value.trim()) {
    ElMessage.warning('回答内容不能为空')
    return
  }

  try {
    await createStudentAnswerAll({
      answerContent: newAnswer.value,
      questionId: route.params.questionId
    })
    
    ElMessage.success('回答提交成功')
    showEditor.value = false
    newAnswer.value = ''
    // 刷新回答列表
    await loadQuestionData();
  } catch (error) {
    ElMessage.error('提交失败：' + error.message)
  }
}
const route = useRoute()
const questionData = ref({
  isLiked: false
})

// 修复后的toggleLike方法
const toggleLike = async (item) => {
  const originalState = item.isLiked
  try {
    item.isLiked = !originalState
    item.likeNumber = item.isLiked ? item.likeNumber + 1 : item.likeNumber - 1
    await updateQuestionLike(item.id, item.isLiked)
  } catch (error) {
    item.isLiked = originalState
    item.likeNumber = originalState ? item.likeNumber + 1 : item.likeNumber - 1
    console.error('点赞操作失败:', error)
  }
}
const toggleAnswerLike = async (item) => {
  const originalState = item.isLiked
  try {
    // 先更新前端状态
    item.isLiked = !originalState
    item.likes = item.isLiked ? item.likes + 1 : item.likes - 1

    // 根据回答类型调用不同接口
    if (item.type === '问题提出者回答') {
      await updateStudentAnswerLike(item.id, item.isLiked)
    } else if (item.type === '改进回答') {
      await updateStudentImproveAnswerLike(item.id, item.isLiked)
    } else {
      await updateStudentAnswerAllLike(item.id, item.isLiked)
    }
  } catch (error) {
    // 失败时回滚状态
    item.isLiked = originalState
    item.likes = originalState ? item.likes + 1 : item.likes - 1
    console.error('点赞操作失败:', error)
  }
}
</script>




<style scoped>
.meta-info {
  color: #909399;
  font-size: 14px;
  margin: 10px 0;
}
.student-info {
  margin-left: 15px;
  color: #606266;
}
.answer-section {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 24px; /* 增加卡片间距 */
}

.answer-card {
  margin-bottom: 16px; /* 增加底部间距 */
  transition: box-shadow 0.3s;
}

.answer-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.answer-content {
  min-height: 60px;
  padding: 16px 0;
  line-height: 1.6;
}

.action-bar {
  margin-top: 12px; /* 点赞按钮与内容间距 */
  padding-top: 12px;
  border-top: 1px solid #eee;
}
</style>
