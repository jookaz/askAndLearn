<template>
  <div class="student-container">
    <el-card class="info-card">
      <h3>👤 个人信息</h3>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="姓名">{{ studentInfo.studentName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ studentInfo.studentNumber }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ studentInfo.classes }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ studentInfo.major }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="wenxue-card">
      <h3>📊 当前课程问学值</h3>
      <div v-loading="loading">
        <el-progress 
          :percentage="wenxueValue"
          :format="formatProgress"
          status="success"
          stroke-width="15"
        />
        <div class="wenxue-detail">
          课程名称：{{ currentCourseName }}
          <br>
          问学值得分：{{ wenxueValue }} 分
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getStudentInfo, getWenxueValue } from '@/api/student'

export default {
  data() {
    return {
      studentInfo: {},
      wenxueValue: 0,
      loading: true,
      currentCourseId: localStorage.getItem('currentCourseId') || ''
    }
  },
  computed: {
    currentCourseName() {
      return this.$store.getters['chapter/getCurrentChapterParams']?.courseName || '未选择课程'
    }
  },
  async mounted() {
    await this.fetchStudentInfo();
    if(this.currentCourseId) {
      await this.fetchWenxueValue();
    } else {
      this.$message.warning('当前未选择课程，无法获取问学值');
    }
    this.loading = false;
  },
  // 添加课程ID变化的监听
  watch: {
      '$store.state.chapter.currentChapterParams': {
      handler(newVal) {
        if(newVal?.courseId) {
          this.currentCourseId = newVal.courseId;
          localStorage.setItem('currentCourseId', newVal.courseId);
        }
      },
      deep: true
    }
  },
  methods: {
    // 在方法中移除无效字段引用
    async fetchStudentInfo() {
      try {
        const res = await getStudentInfo()
        if(res.data?.code === 1) {
          this.studentInfo = res.data.data
          // 存储必要信息到localStorage
          // localStorage.setItem('student-info', res.data.data.studentName)
        }
      } catch (error) {
        this.$message.error('获取学生信息失败')
      }
    },
    async fetchWenxueValue() {
      try {
        const res = await getWenxueValue(this.currentCourseId)
        if(res.data?.code === 1) {
          this.wenxueValue = res.data.data.wenxueValue || 0  // ← 修改字段名称
          this.$message.success('问学值获取成功')
        }
      } catch (error) {
        this.$message.error('获取问学值失败')
      }
    }
  }
}
</script>

<style scoped>
.student-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
}

.info-card, .wenxue-card {
  margin-bottom: 20px;
}

.wenxue-detail {
  margin-top: 15px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 6px;
}

.el-progress {
  margin: 20px 0;
}
</style>