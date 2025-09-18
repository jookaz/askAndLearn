<template>
  <el-dialog
    v-model="visible"
    :title="formTitle"
    width="800px"
  >
    <el-form
      :model="form"
      label-width="100px"
    >
      <!-- 课程图片上传 -->
      <el-form-item label="课程封面">
        <el-upload
          :http-request="handleUpload"
          :before-upload="beforeUpload"
          :show-file-list="false"
          accept=".jpg,.png"
        >
          <el-image 
            v-if="form.image"
            :src="form.image"
            fit="cover"
            class="preview-image"
          />
          <el-button
            v-else
            type="primary"
          >
            点击上传
          </el-button>
        </el-upload>
      </el-form-item>

      <!-- 课程基本信息 -->
      <el-form-item
        label="课程名称"
        prop="courseName"
      >
        <el-input v-model="form.courseName" />
      </el-form-item>
      
      <!-- 新增教师选择 -->
      <el-form-item
        label="授课教师"
        prop="teacherId"
      >
        <el-input 
          v-model="teacherName"
          disabled
          placeholder="当前登录教师"
        />
      </el-form-item>
      
      <el-form-item
        label="课程描述"
        prop="description"
      >
        <el-input
          v-model="form.description"
          type="textarea"
          rows="4"
        />
      </el-form-item>

      <!-- 章节管理 -->
      <el-form-item label="课程章节">
        <div
          v-for="(chapter, index) in form.chapters"
          :key="index"
        >
          <el-input 
            v-model="chapter.chapterName" 
            placeholder="章节名称"
            style="width: 200px; margin-right: 10px;"
          />
          <el-input-number
            v-model="chapter.chapterOrder"
            :min="1"
            controls-position="right"
          />
          <el-button 
            type="danger" 
            icon="el-icon-delete" 
            style="margin-left: 10px;"
            @click="removeChapter(index)"
          >
            删除
          </el-button>
        </div>
        <el-button
          type="primary"
          @click="addChapter"
        >
          添加章节
        </el-button>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">
        取消
      </el-button>
      <el-button
        type="primary"
        @click="submitForm"
      >
        提交
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, computed} from 'vue'  // 添加onMounted导入
import { defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'
import { uploadCourseImage } from '@/api/course'

const props = defineProps({
  modelValue: Boolean,
  courseData: Object
})

const emit = defineEmits(['update:modelValue', 'submit'])

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
const teacherName = ref(localStorage.getItem('teacher-info'))
const teacherId = ref(localStorage.getItem('teacher-id'))
// 表单数据
const form = ref({
  teacherId: teacherId.value,
  courseName: '',
  description: '',
  image: '',
  chapters: []
})

// 初始化表单数据
watch(() => props.courseData, (val) => {
  if (val) {
    form.value = { 
      ...val,
      // 修复章节映射逻辑，保留章节ID
      chapters: val.chapters?.map(c => ({
        id: c.id, // 保留章节ID用于更新
        chapterName: c.chapterName,
        chapterOrder: c.chapterOrder
      })) || []
    }
  }
})

// 表单标题
const formTitle = computed(() => form.value.id ? '编辑课程' : '新建课程')

// 章节操作
const addChapter = () => {
  form.value.chapters.push({
    chapterName: '',
    chapterOrder: form.value.chapters.length + 1
  })
}

const removeChapter = (index) => {
  form.value.chapters.splice(index, 1)
}

// 文件上传处理
const beforeUpload = (file) => {
  const isImage = ['image/jpeg', 'image/png'].includes(file.type)
  if (!isImage) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片!')
    return false
  }
  return true
}

// 替换原有的handleUploadSuccess
const handleUpload = async (options) => {
  try {
    const res = await uploadCourseImage(options.file, 'teacher-course-image')
    if (res.data.code === 1) {
      form.value.image = res.data.data
    }
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

// 提交表单
const submitForm = () => {
  emit('submit', form.value)
  visible.value = false
  
  // 新增表单重置逻辑
  form.value = {
    teacherId: null,
    courseName: '',
    description: '',
    image: '',
    chapters: []
  }
}


</script>