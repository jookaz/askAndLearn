<template>
  <div class="completion-container">
    <el-card class="completion-card">
      <h2 style="color: #67c23a">🎉 他问你答完成</h2>
      <div class="completion-content">
        <p>您已完成本章节所有他问答内容，可进行以下操作：</p>
        <el-button 
          type="primary" 
          @click="backToChapter"
          class="back-btn"
        >
          ← 返回章节列表
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

const router = useRouter()
const store = useStore()

const backToChapter = () => {
  router.push({
    name: 'chapters',
    params: {
      courseId: store.getters['chapter/getCurrentChapterParams'].courseId
    }
  })
}
</script>