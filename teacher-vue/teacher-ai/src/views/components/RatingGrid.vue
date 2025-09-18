<template>
  <div class="rating-container">
    <div 
      v-for="score in 10" 
      :key="score"
      class="rating-box"
      :class="{ active: selectedScore >= score }"
      @click="handleClick(score)"
      @mouseover="hoverScore = score"
      @mouseleave="hoverScore = 0"
    >
      {{ score }}
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const selectedScore = ref(0)
const hoverScore = ref(0)

const emit = defineEmits(['score-selected'])

const handleClick = (score) => {
  selectedScore.value = score
  emit('score-selected', score)
}
</script>

<style scoped>
.rating-container {
  display: flex;
  gap: 2px;
  margin: 20px auto; /* 改为auto居中 */
  background: #fff; /* 修改背景色增强对比 */
  border-radius: 4px;
  padding: 4px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1); /* 添加投影 */
  width: 95%; /* 设置相对宽度 */
}

.rating-box {
  flex: 1; /* 等分宽度 */
  height: 32px;
  border: none;
  background: #f5f7fa;
  transition: all 0.3s ease;
  color: #606266;
  font-weight: bold;
}

.rating-box:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.rating-box.active {
  background: linear-gradient(45deg, #409eff, #79bbff);
  color: white;
}
</style>