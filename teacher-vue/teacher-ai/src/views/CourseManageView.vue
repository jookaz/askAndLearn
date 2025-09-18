<template>
  <div class="course-manage">
    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button
        type="primary"
        @click="handleCreate"
      >
        新建课程
      </el-button>
      <el-button
        type="danger"
        @click="batchDelete"
      >
        批量删除
      </el-button>
    </div>

    <!-- 课程表格 -->
    <el-table 
      v-loading="loading"
      :data="courses"
      border
      stripe
      element-loading-text="数据加载中..."
      empty-text="暂无课程数据"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column
        prop="courseName"
        label="课程名称"
      />
      <el-table-column
        prop="description"
        label="课程描述"
      />
      <el-table-column
        label="操作"
        width="200"
      >
        <template #default="{row}">
          <el-button
            size="small"
            @click="handleEdit(row)"
          >
            编辑
          </el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(row.id)"
          >
            删除
          </el-button>
           <el-button
          size="small"
          type="info"
          @click="handleViewStudents(row.id)"
        >
          查看学生
        </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增学生信息弹窗 -->
    <el-dialog v-model="studentDialogVisible" title="学生列表" width="50%">
      <el-table :data="studentList">
        <el-table-column prop="studentName" label="姓名" />
        <el-table-column prop="classes" label="班级" />
        <el-table-column prop="wenxueValue" label="问学值" />
      </el-table>
    </el-dialog>
    <!-- 分页组件 -->
    <el-pagination
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      background
      :page-sizes="[10, 20, 30]"
      layout="total, sizes, prev, pager, next"
      :total="total"
    />

    <!-- 课程表单弹窗 -->
    <course-form 
      v-model="dialogVisible"
      :course-data="currentCourse"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'  // 添加ElMessage导入
import CourseForm from './components/CourseForm.vue'
// 新增API引用
import { getStudentsByCourse } from '@/api/coursemanage'
// 新增响应式数据
const studentDialogVisible = ref(false)
const studentList = ref([])

// 新增处理方法
const handleViewStudents = async (courseId) => {
  try {
    const res = await getStudentsByCourse(courseId)
    studentList.value = res.data.data
    studentDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取学生数据失败')
  }
}
import { 
  getCoursePage, 
  deleteCourses, 
  createCourse,
  updateCourse,
  getCourseDetail
} from '@/api/coursemanage'  // 修改引用路径

// 分页数据
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)
const courses = ref([])
const loading = ref(false)

// 获取课程列表
const fetchCourses = async () => {
  loading.value = true
  try {
    const res = await getCoursePage({
      pageNo: pageNo.value,
      pageSize: pageSize.value
    })
    courses.value = res.data.data.records || []
    total.value = res.data.data.total || 0
  } catch (error) {
    console.error('获取课程列表失败:', error)
    ElMessage.error('数据加载失败')  // 使用导入后的ElMessage
  } finally {
    loading.value = false
  }
}

// 监听分页变化
watch([pageNo, pageSize], fetchCourses)

// 初始化加载
fetchCourses()

// 批量操作相关
const selectedIds = ref([])
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 删除操作
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该课程吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCourses([id])
    ElMessage.success('删除成功')
    fetchCourses()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

const batchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的课程')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的${selectedIds.value.length}门课程吗？`, '批量删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCourses(selectedIds.value)
    ElMessage.success('批量删除成功')
    fetchCourses()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

// 表单相关
const dialogVisible = ref(false)
const currentCourse = ref(null)

// 监听对话框关闭
watch(dialogVisible, (val) => {
  if (!val) {
    currentCourse.value = null  // 确保下次打开是新建状态
  }
})

const handleCreate = () => {
  currentCourse.value = null
  dialogVisible.value = true
}

const handleEdit = async (course) => {
  try {
    const res = await getCourseDetail(course.id)
    currentCourse.value = {
      ...res.data.data,
      chapters: res.data.data.chapters // 直接使用接口返回的chapters字段
    }
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取课程详情失败')
  }
}

const handleSubmit = async (formData) => {
  try {
    if (formData.id) {
      await updateCourse({
        ...formData,
        image: formData.image,
        chapterList: formData.chapters // 确保字段名与后端一致
      })
    } else {
      await createCourse({
        ...formData,
        image: formData.image // 新建时包含图片字段
      }) 
    }
    fetchCourses()
  } catch (error) {
    console.error('提交失败:', error)
  }
}
</script>