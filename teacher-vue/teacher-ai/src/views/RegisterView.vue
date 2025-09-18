<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2>{{ userType === 'student' ? '学生注册' : '教师注册' }}</h2>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" />
        </el-form-item>

        <el-form-item :label="userType === 'student' ? '学号' : '工号'" prop="number">
          <el-input v-model="form.number" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">立即注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { studentRegister, teacherRegister } from '@/api/auth'  // 新增teacherRegister导入
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
// 移除未使用的router声明
const userType = ref(route.query.type || 'student')

const form = ref({
  userName: '',
  number: '',
  password: ''
})

// const validateNumber = (rule, value, callback) => {
//   if (!/^\d{6}$/.test(value)) {
//     callback(new Error('必须为6位数字'))
//   } else {
//     callback()
//   }
// }

const validatePassword = (rule, value, callback) => {
  if (!/^(?=.*[A-Za-z]).{6,15}$/.test(value)) {
    callback(new Error('6-15位且至少包含一个字母'))
  } else {
    callback()
  }
}

const rules = {
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  number: [
    { required: true, message: '请输入编号', trigger: 'blur' },
    // { validator: validateNumber, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ]
}
// 移除未使用的onMounted导入
const submitForm = async () => {
  try {
    let res
    if (userType.value === 'student') {
      res = await studentRegister({
        userName: form.value.userName,
        studentNumber: form.value.number,
        password: form.value.password
      })
    } else {
      res = await teacherRegister({
        userName: form.value.userName,
        workerNumber: form.value.number,
        password: form.value.password
      })
    }
    
    // 修改部分开始
    if (res.data.code === 1) {
      ElMessage.success('注册成功')
      router.push('/login')
    } else {
      ElMessage.error(res.data.msg || '注册失败') // 使用后端返回的msg
    }
    // 修改部分结束
  } catch (error) {
    console.error('注册请求失败:', error)
    ElMessage.error(error.response?.data?.msg || '请求异常，请稍后重试') // 捕获异常中的msg
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.register-card {
  width: 500px;
  padding: 20px;
}
</style>