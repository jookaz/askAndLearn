import request from '@/utils/request'

export const studentLogin = data => request({
  url: '/student/student/login',
  method: 'post',
  data
})

export const teacherLogin = data => request({
  url: '/teacher/teacher/login',
  method: 'post',
  data
})

export const studentRegister = (data) => {
  return request({
    url: '/student/student/register',
    method: 'post',
    data
  })
}

export const teacherRegister = (data) => {
  return request({
    url: '/teacher/teacher/register',
    method: 'post',
    data
  })
}

export function adminLogin(data) {
  return request({
    url: 'admin/admin/login',
    method: 'post',
    data
  })
}