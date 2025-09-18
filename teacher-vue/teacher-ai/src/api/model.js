import request from '@/utils/request'

export const getModelPage = (params) =>
  request.get('/teacher/model', { params })

export const deleteModels = (ids) =>
  request.delete('/teacher/model', {
    params: { ids },
    paramsSerializer: {
      indexes: null // 禁用数组索引格式
    }
  })

export const createModel = (data) =>
  request.post('/teacher/model', data)

export const updateModel = (data) =>
  request.put('/teacher/model', data)

export const getModelDetail = (id) =>
  request.get(`/teacher/model/${id}`)