<template>
  <div class="improve-container">
    <!-- 新增空状态提示 -->
    <el-card v-if="questions.length === 0" class="empty-card">
      <h3 style="color: #909399">📭 当前章节暂无问题</h3>
      <div class="empty-content">
        <p>当前章节还没有学生提出问题，您可以选择：</p>
        <el-button 
          type="primary"
          @click="backToChapter"
          class="back-btn"
        >
          返回章节列表
        </el-button>
      </div>
    </el-card>

    <!-- 原有问题列表 -->
    <el-card 
      v-for="q in questions" 
      :key="q.id"
      class="question-card"
      @click="$router.push({ 
        name: 'QuestionWithAllAnswer',
        params: { questionId: q.id }
      })"
    >
      <div class="question-box">
        <strong class="question-content">{{ q.questionContent }}</strong>
        <div class="student-info">
          <span>{{ q.studentName }}</span>
          <span style="margin:0 10px">|</span>
          <span>{{ q.classes }}</span>
        </div>
        <div class="action-bar">
          <el-button @click.stop="toggleLike(q)">
            <el-icon :class="{ 'liked': q.isLiked }">
              <CustomThumbIcon v-if="!q.isLiked" />
              <CustomThumbIcon v-else color="#409eff" />
            </el-icon>
            <span class="count">{{ q.likes }}</span>
          </el-button>
        </div>
      </div>
      <el-divider />
    </el-card>
  </div>
</template>

<script setup>
// 新增导入
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ref, onMounted } from 'vue'
import { listAllQuestions, updateQuestionLike } from '@/api/askyou'
import CustomThumbIcon from '@/components/CustomThumbIcon.vue'
const questions = ref([])
const router = useRouter()
const store = useStore()

// 新增返回章节方法（参考CompletionPage.vue）
const backToChapter = () => {
  if (window.parent.forceRefreshProgress) {
    window.parent.forceRefreshProgress();
  }
  router.push({
    name: 'chapters',
    params: {
      courseId: store.getters['chapter/getCurrentChapterParams'].courseId
    }
  });
}
onMounted(async () => {
  try {
    const { courseId, chapterId } = store.getters['chapter/getCurrentChapterParams']
    const res = await listAllQuestions({ 
      courseId,
      chapterId
    })
    questions.value = res.data.data.map(q => ({
      ...q,
      likes: q.likeNumber, // 使用后端返回的点赞数
      isLiked: false
    }))
  } catch (error) {
    console.error('获取问题列表失败:', error)
  }
})

const toggleLike = async (item) => {
  const originalState = item.isLiked  // 将声明移至函数作用域顶部
  try {
    // 立即更新UI状态
    item.isLiked = !originalState
    item.likes = item.isLiked ? item.likes + 1 : item.likes - 1
    
    // 调用后端接口
    await updateQuestionLike(item.id, item.isLiked)
  } catch (error) {
    // 出错时回滚状态
    item.isLiked = originalState
    item.likes = originalState ? item.likes + 1 : item.likes - 1
    console.error('点赞操作失败:', error)
  }
}
</script>

<style scoped>
.student-info {
  color: #909399;
  font-size: 14px;
  margin-bottom: 10px;
}
.empty-card {
  margin-bottom: 20px;
  text-align: center;
  padding: 40px 20px;
}

.empty-content {
  margin: 20px 0;
}

.back-btn {
  margin-top: 15px;
}

.question-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.question-content {
  font-size: 16px;
  line-height: 1.6;
  display: block;
  margin-bottom: 15px;
}

.action-bar {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.el-divider {
  margin: 15px 0;
  background-color: #ebeef5;
}
</style>