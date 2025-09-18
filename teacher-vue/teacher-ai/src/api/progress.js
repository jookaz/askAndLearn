import request from '@/utils/request'

export const getOrCreateProgress = (params) => {
  return request({
    url: '/student/user-chapter-progress/get-or-create',
    method: 'get',
    params
  })
}

export const updateProgressStatus = (data) => {
  return request({
    url: '/student/user-chapter-progress/update',
    method: 'post',
    data
  })
}