export const getChapterDetail = (chapterId) => {
  return request({
    url: `/teacher/chapters/${chapterId}`,
    method: 'get'
  })
}