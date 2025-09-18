import request from '@/utils/request'
import service from '@/utils/request'  // 添加axios实例导入

// 创建课程
export const createCourse = (data) => {
  return request({
    url: '/teacher/courses',
    method: 'POST',
    data  // 使用请求体传递CourseDTO对象
  })
}

// 更新课程
export const updateCourse = (data) => {
  return request({
    url: '/teacher/courses',
    method: 'PUT',  // 改为PUT方法
    data
  })
}

// 批量删除
export const deleteCourses = (ids) => {
  return service.delete('/teacher/courses', {
    params: {
      ids: ids.join(',')  // 将数组转换为逗号分隔字符串
    },
    paramsSerializer: {
      indexes: null  // 禁用数组索引格式
    }
  })
}

// 分页查询
export const getCoursePage = (params) => {
  return request({
    url: '/student/courses/page',
    method: 'GET',
    params: {
      ...params,
      sort: params.sort || 'update_time',
      isAsc: params.isAsc || false
    }
  })
}

export const getCourseDetail = (courseId) => {
  return request({
    url: `/student/courses/${courseId}`,
    method: 'get'
  })
}

export const getChapterDetail = (chapterId) => {
  return request({
    url: `/student/chapters/${chapterId}`,
    method: 'get'
  })
}
// 新增章节进度接口

// 获取所有章节进度接口（新增）
export const getAllChapterProgress = (params) => {
  return request({
    url: '/student/user-chapter-progress/list-by-user',
    method: 'get',
    params  // 使用URL参数传递查询条件
  })
}

// 获取单章进度接口（修正）
export const getChapterProgress = (dto) => {
  return request({
    url: '/student/user-chapter-progress/get-or-create',
    method: 'get',
    params: dto // 保持与现有项目一致的参数传递方式
  })
}
export const getQuestionByParams = (params) => {
  return request({
    url: '/student/question/get-by-id',
    method: 'get',
    params
  });
}
export const listQuestions = (params) => {
  return request({
    url: '/student/question/list',
    method: 'get',
    params
  })
}
export const uploadCourseImage = (file, folderName) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('folderName', folderName)
  return service.post('/teacher/common/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}



export const getCourseCount = () => {
  return request({
    url: '/teacher/courses/count',
    method: 'get'
  })
}