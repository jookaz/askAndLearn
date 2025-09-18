import request from '@/utils/request'

export const getModelPage = (params) => {
  return request({
    url: '/admin/model',
    method: 'get',
    params
  })
}

export const deleteModels = (ids) =>
  request.delete('/admin/model', {
    params: { ids },
    paramsSerializer: {
      indexes: null // 禁用数组索引格式
    }
  })

export const createModel = (data) => {
  return request({
    url: '/admin/model',
    method: 'post',
    data
  })
}

export const updateModel = (data) => {
  return request({
    url: '/admin/model',
    method: 'put',
    data
  })
}

export const getModelDetail = (id) => {
  return request({
    url: `/admin/model/${id}`,
    method: 'get'
  })
}
export const setSystemModel = (modelId) => {
  return request({
    url: `/admin/model/setSystemModel`,
    method: 'put',
    params: { modelId }
  })
}

export const getPromptPage = (params) =>
  request.get('/admin/prompt-template/page', { params })

export const addPrompt = (data) =>
  request.post('/admin/prompt-template', data)

export const updatePrompt = (data) =>
  request.put('/admin/prompt-template', data)

export const deletePrompts = (ids) =>
  request.delete('/admin/prompt-template', {
    params: { ids },
    paramsSerializer: { indexes: null }
  })

export const getPromptDetail = (id) =>
  request.get(`/admin/prompt-template/${id}`)