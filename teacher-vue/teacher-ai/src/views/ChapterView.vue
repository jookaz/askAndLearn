<template>
  <div class="chapter-container">
    <!-- 课程标题 -->
    <div class="header-section">
      <el-button 
        type="primary" 
        class="back-btn"
        icon="el-icon-arrow-left"
        @click="$router.push('/courses')"
      >
        返回课程列表
      </el-button>
      <h2 class="course-title">
        {{ courseInfo.courseName }}
      </h2>
      <div class="teacher-info">
        <el-icon><User /></el-icon>
        <span class="teacher-name">任课老师：{{ courseInfo.teacherName }}</span>
      </div>
    </div>
    
    <!-- 章节列表 -->
    <el-card class="chapter-list">
      <el-row :gutter="20">
        <el-col 
          v-for="chapter in chapters" 
          :key="`chapter-${chapter.id}`"  
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
        >
          <div class="chapter-link">
            <el-card
              class="chapter-card"
              shadow="hover"
              @click="handleChapterClick(chapter.id)"
            >
              <!-- 删除以下复选框代码 -->
              <div class="chapter-order">
                第{{ chapter.chapterOrder }}章
              </div>
              <h3 class="chapter-name">
                {{ chapter.chapterName }}
              </h3>
              <el-progress 
                :percentage="(progressData[chapter.id]?.step1Complete || 0) * 33.33 + 
                            (progressData[chapter.id]?.step2Complete || 0) * 33.33 + 
                            (progressData[chapter.id]?.step3Complete || 0) * 33.33"
                :status="progressData[chapter.id]?.step3Complete === 1 ? 'success' : ''"
                style="margin-top:10px"
              />
            </el-card>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { 
  getCourseDetail, 
  getChapterProgress,
  getAllChapterProgress,
  getQuestionByParams,
  getChapterDetail // 新增接口导入
} from '@/api/course'


export default {
  computed: {
    courseId() {
      return this.$route.params.courseId
    }
  },
  data() {
    return {
      selectedChapters: {},
      chapters: [],
      courseInfo: {},
      progressData: {} // 新增进度数据
    }
  },
  mounted() {
    this.forceRefreshProgress() // 调用刷新方法
     // 在数据加载后初始化勾选状态
    this.$nextTick(() => {
      this.chapters.forEach(chapter => {
        this.$set(this.selectedChapters, chapter.id, false)
      })
    })
    // 在mounted中增加
    
  },
 // 正确放置watch监听
 watch: {
    '$route.params.courseId': {
      immediate: true,
      handler(newVal) {
        if (newVal) this.fetchAllChapterProgress()
      }
    }
  },
  methods: {
    // 新增自动刷新方法
  // 新增强制刷新方法
  async forceRefreshProgress() {
    await this.fetchCourseDetail();
    await this.fetchAllChapterProgress();
    this.chapters.forEach(chapter => {
      this.fetchProgress(chapter.id);
    });
  },
    // 简化章节选中逻辑
    handleCheckboxChange(chapter) {
      this.$store.commit('chapter/setSelectedChapter', {
        chapterId: chapter.id,
        isSelected: true
      });
    },
    
    // 正确的方法定义层级
    // 在methods中添加
    async fetchChapterDetail(chapterId) {
      try {
        const res = await getChapterDetail(chapterId) // 需要先在api中定义
        if (res.data.code === 1) {
          return res.data.data
        }
        return null
      } catch (error) {
        console.error('获取章节详情失败:', error)
        this.$message.error('章节详情加载失败')
      }
    },
    
    async handleChapterClick(chapterId) {
      const controller = new AbortController();
      
      try {
        const [detailRes, questionData] = await Promise.all([
          getChapterDetail(chapterId, { signal: controller.signal }),
          this.fetchQuestion(chapterId)
        ]);
    
        const chapterData = detailRes.data.data;
        
        try {
          await this.$confirm(
            `已选择第${chapterData.chapterOrder}章`,
            '选择成功',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              showCancelButton: true,
              type: 'success'
            }
          );
    
          // 用户确认后的逻辑
          this.$store.commit('chapter/setCurrentChapterParams', {
            userId: Number(localStorage.getItem('student-id')),
            courseId: this.courseId,
            chapterId,
            courseName: chapterData.courseName,
            chapterName: chapterData.chapterName,
            chapterOrder: chapterData.chapterOrder,
            questionData,
            progress: this.progressData[chapterId] || {}
          });
          // 新增本地存储课程ID
          localStorage.setItem('currentCourseId', this.courseId);  // ← 添加这行
          this.$root.setCurrentIds(this.courseId, chapterId);
          await this.fetchProgress(chapterId);
          const chapter = this.chapters.find(c => c.id === chapterId);
          this.handleCheckboxChange(chapter);
    
        } catch (confirmError) {
          if (confirmError === 'cancel') {
            controller.abort();
            this.$message.warning('操作已取消');
            return;
          }
          throw confirmError;
        }
    
      } catch (error) {
        if (error.name === 'AbortError') {
          console.log('请求已被中止');
        } else {
          console.error('处理失败:', error);
          this.$message.error(`操作失败: ${error.message}`);
        }
      }
    },

    
    // 修复方法定义语法
    toggleChapterSelection(chapterId) {
      this.setSelectedChapter({
        chapterId,
        isSelected: !this.getSelectedChapters(chapterId)
      });
    },
    // 新增获取全部章节进度方法
    async fetchAllChapterProgress() {
      try {
        // 修改为使用API模块的规范调用
        const res = await getAllChapterProgress({
          userId: Number(localStorage.getItem('student-id')),
          courseId: this.courseId
        });
        
        if (res.data.code === 1) {
          this.progressData = res.data.data.reduce((acc, curr) => {
            acc[curr.chapterId] = {
              step1Complete: Number(curr.step1Complete),
              step2Complete: Number(curr.step2Complete),
              step3Complete: Number(curr.step3Complete)
            };
            return acc;
          }, {});
           // 提交到Vuex store
           this.$store.commit('chapter/setChapterProgress', res.data.data);
        }
      } catch (error) {
        console.error('获取章节进度失败:', error);
        this.$message.error('进度加载失败'); // 新增错误提示
      }
    },
    // 新增进度获取方法
    async fetchProgress(chapterId) {
      try {
        const res = await getChapterProgress({
          userId: Number(localStorage.getItem('student-id')),
          courseId: this.courseId,
          chapterId: chapterId
        })
        // 精确解构需要的字段
        const { 
          step1Complete = 0, 
          step2Complete = 0, 
          step3Complete = 0 
        } = res.data?.data || {};
        
        // 修改为直接赋值
        this.progressData[chapterId] = {
          step1Complete: Number(step1Complete),
          step2Complete: Number(step2Complete),
          step3Complete: Number(step3Complete)
        }
      } catch (error) {
        console.error('进度获取失败:', error)
      }
    },

    async fetchCourseDetail() {
      try {
        const res = await getCourseDetail(this.courseId)  // 确保此方法被调用
        if (res.data.code === 1) {
          this.courseInfo = res.data.data
          this.chapters = res.data.data.chapters.sort((a, b) => a.chapterOrder - b.chapterOrder)
        }
      } catch (error) {
        console.error('获取课程详情失败:', error)
        this.$message.error('课程加载失败')
      }
    },
    // 在methods中添加获取问题方法
    async fetchQuestion(chapterId) {
      try {
        const res = await getQuestionByParams({
          userId: Number(localStorage.getItem('student-id')),
          courseId: this.courseId,
          chapterId: chapterId
        });
        if (res.data.code === 1) {
          return res.data.data;
        }
        return null;
      } catch (error) {
        console.error('获取问题失败:', error);
        this.$message.error('问题加载失败');
        return null;
      }
    }

    
   
  }
}
</script>

<style scoped>
.chapter-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.course-title {
  text-align: center;
  color: #303133;
  margin-bottom: 30px;
}

.chapter-list {
  margin-top: 20px;
}

.chapter-link {
  text-decoration: none;
}

.chapter-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
  cursor: pointer;
}

.chapter-card:hover {
  transform: translateY(-3px);
}

.chapter-order {
  color: #409eff;
  font-size: 14px;
  margin-bottom: 8px;
}

.chapter-name {
  font-size: 16px;
  color: #606266;
  margin: 0;
}

.header-section {
  position: relative;
  margin-bottom: 30px;
  text-align: center;
}

.back-btn {
  position: absolute;
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
  border-radius: 18px;
  padding: 10px 20px;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.back-btn:hover {
  transform: translateY(-50%) scale(1.05);
}

.teacher-info {
  margin-top: 15px;
  font-size: 16px;
  color: #606266;
  display: inline-flex;
  align-items: center;
  background: #f5f7fa;
  padding: 8px 20px;
  border-radius: 20px;
}

.teacher-info .el-icon {
  margin-right: 8px;
  color: #409eff;
}

.teacher-name {
  font-weight: 500;
}
.chapter-checkbox {
  position: absolute;
  right: 10px;
  top: 10px;
  z-index: 999;
  

}
</style>


