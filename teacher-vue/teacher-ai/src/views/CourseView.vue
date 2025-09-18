<template>
  <div class="course-container">
    <!-- 课程书架布局 -->
    <el-row
      :gutter="20"
      class="course-list"
    >
      <el-col 
        v-for="course in courses" 
        :key="`course-${course.id}`" 
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <el-card 
          class="course-card"
          @click="handleCourseClick(course.id)"
        >
          <el-image
            :src="course.image || 'https://java-aitwo.oss-cn-beijing.aliyuncs.com/teacher-course-image/0be790dc-a3f8-4eda-948a-be2592c2f416.jpg'"
            fit="cover"
            class="course-image"
          />
          <div class="course-info">
            <h3 class="course-name">
              {{ course.courseName }}
            </h3>
            <p class="course-desc">
              {{ course.description }}
            </p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页组件 -->
    <el-pagination
      background
      layout="prev, pager, next, sizes"
      :total="total"
      :page-sizes="[8, 16, 24]"
      :page-size="pageSize"
      :current-page="pageNo"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { getCoursePage } from '@/api/course'

export default {
  data() {
    return {
      courses: [],
      total: 0,
      pageNo: 1,
      pageSize: 8
    }
  },
  mounted() {
    this.fetchCourses()
  },
  methods: {
    async fetchCourses() {
      try {
        const params = {
          pageNo: this.pageNo,
          pageSize: this.pageSize,
          sort: 'update_time',  // 修正排序字段为update_time
          isAsc: false
        }
        const res = await getCoursePage(params)
        
        if (res.data.code !== 1) {
          throw new Error(res.message || '获取课程失败1')
        }
        
        this.courses = res.data.data.records  // 调整为data.data.records
        this.total = res.data.data.total      // 调整为data.data.total
      } catch (error) {
        console.error('获取课程失败2:', error)
        this.$message.error(error.message)
      }
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.fetchCourses()
    },
    handleCurrentChange(page) {
      this.pageNo = page
      this.fetchCourses()
    },
    handleCourseClick(courseId) {
      this.$router.push(`/courses/${String(courseId)}/chapters`)  // 强制转换为字符串
    }
  }
}
</script>

<style scoped>
.course-container {
  padding: 20px;
}

.course-list {
  margin-bottom: 20px;
}

.course-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.course-card:hover {
  transform: translateY(-5px);
}

.course-image {
  height: 200px;
  width: 100%;
}

.course-info {
  padding: 15px;
}

.course-name {
  font-size: 16px;
  margin-bottom: 10px;
  color: #303133;
}

.course-desc {
  font-size: 14px;
  color: #909399;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}
</style>