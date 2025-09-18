import axios from 'axios'

const service = axios.create({
  // baseURL: 'http://47.105.124.57:9090',
  baseURL: 'http://localhost:8080',
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const studentToken = localStorage.getItem('student-token')
    const teacherToken = localStorage.getItem('teacher-token')
    const adminToken = localStorage.getItem('admin-token')
    
    if (studentToken) {
      config.headers['student-token'] = studentToken
    } else if (teacherToken) {
      config.headers['teacher-token'] = teacherToken
    }
    else if (adminToken) {
      config.headers['admin-token'] = adminToken
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 在响应拦截器中添加
service.interceptors.response.use(
  response => response,
  error => {
    if (error.response.status === 401) {
      localStorage.removeItem('student-token')
      localStorage.removeItem('teacher-token')
      localStorage.removeItem('admin-token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default service