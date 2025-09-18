import request from '@/utils/request'

export const getAvailableModels = (courseId) => {
  return request({
    url: `/student/youAsk/models/${courseId}`,
    method: 'get'
  })
}

export const multiModelChat = (modelNames, question, userId, courseId, chapterId, questionId) => {
  return request({
    url: '/student/youAsk/chat/multi',
    method: 'post',
    data: {
      modelNames,
      question,
      userId,
      courseId, 
      chapterId,
      questionId // 添加questionId参数
    },
    timeout: 60000
  })
}

export const streamMultiModelChat = (modelNames, prompt) => {
  return request({
    url: '/student/youAsk/chat/multi',
    method: 'post',
    data: { modelNames, prompt },
    responseType: 'text',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'text/event-stream'
    }
  })
};

export const postChat = (data, config) => {
  return request({
    url: '/student/youAsk/chat',
    method: 'post',
    data,
    ...config  // 合并额外配置
  })
}

export const chatAnswer = (params) => {
  return request({
    url: '/student/youAsk/chat/answer',
    method: 'post',
    timeout: 60000,
    data: params
  })
}

export const updateModelScore = (id, score) => {
  return request({
    url: '/student/modelsAnswer/score',
    method: 'put',
    data: {
      id,
      studentScore: score
    }
  })
}

// 添加以下接口定义
export const getChatIds = () => {
  return request({
    url: '/ai/history',
    method: 'get'
  })
}

export const getChatHistory = (chatId) => {
  return request({
    url: `/ai/history/${chatId}`,
    method: 'get'
  })
}

export const listAllQuestions = (params) => {
  return request({
    url: '/student/question/listAll',
    method: 'get',
    params
  })
}

export function updateQuestionLike(questionId, isLike) {
  return request({
    url: '/student/question/updateLike',
    method: 'put',
    params: { questionId, isLike }
  })
}

export function getQuestionAllAnswers(questionId) {
  return request({
    url: '/student/question/allAnswer',
    method: 'get',
    params: { questionId }
  })
}

export const updateStudentAnswerLike = (answerId, isLike) => {
  return request({
    url: 'student/studentAnswer/updateLike',
    method: 'put',
    params: { answerId, isLike }
  })
}

export const updateStudentImproveAnswerLike = (improveAnswerId, isLike) => {
  return request({
    url: 'student/studentImproveAnswer/updateLike',
    method: 'put',
    params: { improveAnswerId, isLike }
  })
}

export const updateStudentAnswerAllLike = (studentAnswerAllId, isLike) => {
  return request({
    url: 'student/studentAnswerAll/updateLike',
    method: 'put',
    params: { studentAnswerAllId, isLike }
  })
}

export const createStudentAnswerAll = (data) => {
  return request({
    url: 'student/studentAnswerAll/create',
    method: 'post',
    data
  })
}

export const updateStudentReason = (data) => {
  return request({
    url: '/student/modelsAnswer/reason',
    method: 'put',
    data
  })
}