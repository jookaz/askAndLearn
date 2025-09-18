import request from '@/utils/request'

export function getTeacherOptions() {
  return request({
    url: '/teacher/teacher/select',
    method: 'get'
  })
}

export const getQuestionPage = (params) => {
  return request({
    url: '/teacher/question/page',
    method: 'get',
    params
  })
}

export const getCourseNames = () => {
  return request({
    url: '/teacher/courses/names',
    method: 'get'
  })
}

export const getChapterNames = (courseId) => {
  return request({
    url: '/teacher/chapters/names',
    method: 'get',
    params: { courseId }
  })
}

export function getAllAnswer(questionId) {
  return request({
    url: '/teacher/question/allAnswer',
    method: 'get',
    params: { questionId }
  })
}

export function archiveQuestions(data) {
  return request({
    url: '/teacher/archive-questions',
    method: 'post',
    data
  })
}

export function archiveAnswers(data) {
  return request({
    url: '/teacher/archive-answers',
    method: 'post',
    data
  })
}

export const createQuestion = (data) => request({
  url: '/teacher/archive-questions',
  method: 'post',
  data
})

export const createAnswer = (data) => request({
  url: '/teacher/archive-answers',
  method: 'post',
  data
})