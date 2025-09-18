import service from '@/utils/request'

export const getCoursePage = (params) => {
  return service({
    url: '/teacher/courses/page',
    method: 'GET',
    params
  })
}

export const deleteCourses = (ids) => {
  return service.delete('/teacher/courses', {
    params: { ids: ids.join(',') },
    paramsSerializer: { indexes: null }
  })
}

export const createCourse = (data) => {
  return service({
    url: '/teacher/courses',
    method: 'POST',
    data
  })
}

export const updateCourse = (data) => {
  return service({
    url: '/teacher/courses',
    method: 'PUT', 
    data
  })
}

export const getCourseDetail = (courseId) => {
  return service({
    url: `/teacher/courses/${courseId}`,
    method: 'get'
  })
}

export const getStudentsByCourse = (courseId) => {
  return service({
    url: `/student-course-relation/get-student-by-course-id/${courseId}`,
    method: 'get'
  })
}