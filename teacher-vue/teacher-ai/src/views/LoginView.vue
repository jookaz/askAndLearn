<template>
  <div class="login-container">
    <div class="background-wrapper">
      <!-- 在此处添加背景图片/视频 -->
      <img src="@/assets/login-bg.jpg" class="background-image" />
    </div>
    <el-card class="login-card">
      <div class="logo-wrapper">
        <img src="@/assets/教育.png" class="logo" />
      </div>
      <h2 class="title">智慧问学系统</h2>
      <h2>系统登录</h2>
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户类型">
          <el-radio-group v-model="userType">
            <el-radio label="student">学生</el-radio>
            <el-radio label="teacher">教师</el-radio>
            <el-radio label="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item :label="userType === 'student' ? '学号' : '工号'">
          <el-input 
            v-model="form.username" 
            placeholder="请输入您的编号"
        
          />
        </el-form-item>
        
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password/>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
          <el-button @click="handleRegister">快速注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { studentLogin, teacherLogin ,adminLogin} from '@/api/auth'
import { ElMessage} from 'element-plus'

const router = useRouter()
const userType = ref('student')
const form = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  try {
    let res
    if (userType.value === 'student') {
      res = await studentLogin({
        studentNumber: form.value.username,
        password: form.value.password
      })
    } else if (userType.value ==='admin') {
      res = await adminLogin({
        workerNumber: form.value.username,
        password: form.value.password
      })
    }
    else {
      res = await teacherLogin({
        workerNumber: form.value.username,
        password: form.value.password
      })
    }

    if (res.data.code !== 1) {
      throw new Error(res.data.msg || '登录失败')
    }

    // 仅在登录成功时处理数据存储
    if (userType.value === 'student') {
      localStorage.setItem('student-token', res.data.data.token)
      localStorage.setItem('student-info', res.data.data.studentName)
      localStorage.setItem('student-id', res.data.data.id)
    }
    else if (userType.value ==='admin') {
      localStorage.setItem('admin-token', res.data.data.token)
      localStorage.setItem('admin-info', res.data.data.adminName)
      localStorage.setItem('admin-id', res.data.data.id)
    } 
    else {
      localStorage.setItem('teacher-token', res.data.data.token)
      localStorage.setItem('teacher-info', res.data.data.teacherName)
      localStorage.setItem('teacher-id', res.data.data.id)
    }

    router.push(localStorage.getItem('admin-token') ? '/system-model' : '/')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error(error.message)
    // 清空敏感信息
    form.value.password = ''
    form.value.username = ''
  }
}

// 添加注册跳转方法
const handleRegister = () => {
  if (userType.value === 'admin') {
    ElMessage.error('管理员账号不可自行注册')
    return
  }
  router.push(`/register?type=${userType.value}`)
}

// const validateNumber = () => {
//   if (!/^\d{6}$/.test(form.value.username)) {
//     ElMessage.warning('请输入6位纯数字编号')
//     form.value.username = ''
//   }
// }
</script>


<style scoped>
.login-container {
  position: relative;
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-card {
  position: relative;
  width: 450px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
  background: rgba(255,255,255,0.95);
  z-index: 1; /* 确保卡片在背景之上 */
}

.background-wrapper {
  position: absolute;
  width: 100%;
  height: 100%;
}

.background-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: blur(2px);
  opacity: 0.8;
}

.logo-wrapper {
  text-align: center;
  margin-bottom: 20px;
}

.logo {
  width: 80px;
  height: 80px;
}

.title {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 30px;
  font-size: 24px;
}
</style>