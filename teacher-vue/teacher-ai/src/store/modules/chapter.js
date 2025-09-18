export default {
  namespaced: true,
  state: () => ({
    // 移除重复的currentChapter定义
    currentChapterParams: {
      courseId: null,
      chapterId: null,
      courseName: '',
      chapterName: '',
      chapterOrder: 0,
      questionData: null,
      progress: {}
    },
    // 新增选中章节状态存储
    selectedChapters: {}
  }),
  mutations: {
    setCurrentChapterParams(state, payload) {
      state.currentChapterParams = payload
    },
    // 新增选中章节的mutation
    setSelectedChapter(state, payload) {
      state.selectedChapters[payload.chapterId] = payload.isSelected
    },
    // 新增mutation
    setChapterProgress(state, progressData) {
        state.currentChapterParams.progress = progressData.reduce((acc, curr) => {
            acc[curr.chapterId] = {
                step1Complete: Number(curr.step1Complete),
                step2Complete: Number(curr.step2Complete),
                step3Complete: Number(curr.step3Complete)
            };
            return acc;
        }, {});
    },
    setQuestionData(state, payload) {
      state.currentChapterParams.questionData = payload;
    }
  },
  getters: {
    getCurrentChapterParams: (state) => state.currentChapterParams,
    // 新增缺失的getter
    getSelectedChapters: (state) => (chapterId) => state.selectedChapters[chapterId]
  }
}
