import request from '@/utils/request'

export const getStudentInfo = () => {
  return request({
    url: '/student/student/get-by-id',
    method: 'get'
  })
}

export function getWenxueValue(courseId) {
  return request({
    url: '/student/student-chapter-wenxue-value/get-by-courseId',
    method: 'get',
    params: { courseId }
  })
}

export const getModelAnswers = (params) => {
  return request({
    url: '/student/archive/page',
    method: 'get',
    params
  })
}