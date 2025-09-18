<template>
   <!-- 分离路由视图和布局容器 -->
   <router-view v-slot="{ Component, route }" v-if="$route.meta.isFullPage">
    <component
      :is="Component"
      :key="route.path"
      :class="{ 'full-page': route.meta.isFullPage }"
    />
  </router-view>
  
  <el-container v-if="!$route.meta.isFullPage" class="main-container">
    <!-- 顶部导航栏 -->
    <el-header class="main-header">
      <div class="header-content">
        <h1 class="system-title">
          智慧问答系统
        </h1>
        <div class="selection-status">
          <span v-if="isStudent && $root.globalState.currentCourseId">
            📖 当前课程：{{ currentCourseName }}
            <template v-if="currentChapterOrder > 0">
              第{{ currentChapterOrder }}章
            </template>
            {{ currentChapterName }}
            👤 {{ userName }}
          </span>
          <span v-else-if="isTeacher || isAdmin">
            👤 {{ userName }}
          </span>
          <span v-else>
            🚩 请选择课程
            👤 {{ userName }}
          </span>
          <span class="user-info">
            <a 
              href="javascript:;" 
              class="logout-link"
              @click="handleLogout"
            >
              退出登录
            </a>
          </span>
        </div>
      </div>
    </el-header>

    <el-container>
      <!-- 左侧菜单 -->
      <el-aside
        width="220px"
        class="side-menu"
      >
        <el-menu
          :default-active="activeMenu"
          :key="menuKey"
          class="el-menu-vertical"
          active-text-color="#409eff"
          background-color="#fafafa"
          @select="handleMenuSelect"
        >
          <el-menu-item index="1" v-if="isStudent || isTeacher">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          
          <el-menu-item index="2" v-if="isStudent">
            <el-icon><Reading /></el-icon>
            <span>课程选择</span>
          </el-menu-item>
          
          <el-menu-item index="4" v-if="isTeacher">
            <el-icon><Notebook /></el-icon>
            <span>课程管理</span>
          </el-menu-item>
          
          <el-divider class="menu-divider" />
          
          <el-submenu index="3" v-if="isStudent">
            <template #title>
              <el-icon><ChatLineRound /></el-icon>
              <span>问答管理</span>
            </template>
            <el-menu-item index="3-1">
              <el-icon><QuestionFilled /></el-icon>
              <span>你问你答</span>
            </el-menu-item>
            <el-menu-item index="3-2">
              <el-icon><ChatRound /></el-icon>
              <span>他问你答</span>
            </el-menu-item>
            <el-menu-item index="5" v-if="isStudent">
              <el-icon><Opportunity /></el-icon>
              <span>协同改进</span>
           </el-menu-item>
         
          </el-submenu>
          <el-menu-item index="6" v-if="isTeacher">
              <el-icon><List /></el-icon>
              <span>问答归档</span>
          </el-menu-item>
          <el-menu-item index="7" v-if="isTeacher">
            <el-icon><Cpu /></el-icon>
            <span>模型管理</span>
          
          </el-menu-item>
          <!-- 在el-submenu index="3"内添加 -->
          <!-- <el-menu-item index="8" v-if="isStudent">
            <el-icon><ChatLineRound /></el-icon>
            <span>多轮对话</span>
          </el-menu-item> -->
          <el-menu-item index="9" v-if="isStudent">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="10" v-if="isAdmin">
              <el-icon><Setting /></el-icon>
              <span>系统模型管理</span>
          </el-menu-item>
          <el-menu-item index="11" v-if="isAdmin">
            <el-icon><Document /></el-icon>
            <span>系统提示词管理</span>
          </el-menu-item>
          <el-menu-item index="12" v-if="isStudent">
            <el-icon><Document /></el-icon>
            <span>模范问答</span>
          </el-menu-item>
          
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>



<script>
import { Notebook } from '@element-plus/icons-vue'
import { getOrCreateProgress } from '@/api/progress';
import { getQuestionByParams } from '@/api/course';

export default {
  name: 'App',
  provide() {
    return {
      appState: this.globalState
    }
  },
  components: {
    Notebook
  },
  data() {
    return {
      currentPage: '首页',
      menuKey: 0,  // 新增菜单组件key
      globalState: {
        currentCourseId: '',  // 修改为空字符串
        currentChapterId: ''  // 修改为空字符串
      }
    }
  },
  computed: {
    isStudent() {
    return !!localStorage.getItem('student-token');
    },
    isTeacher() {
      return !!localStorage.getItem('teacher-token');
    },
    isAdmin() {
      return !!localStorage.getItem('admin-token');
    },
    activeMenu() {
      const routeName = this.$route.name;
      
      return {
        'home': '1',
        'courses': '2',       
        'chapters': '2',      
        'ask': '3-1',          
        'chapter-detail': '3-1', 
        'ImproveAnswer': '3-1',  
        'AnswerSomeone': '3-2',
        'QuestionDetail': '3-2',
        'qa-self': '3-1',     
        'course-manage': '4',
        'CompletionPage': '3-1',      // 新增你问你答完成页面对应菜单
        'completionSomeAskPage': '3-2', // 新增他问你答完成页面对应菜单
        'CollaborativeImprovement': '5',
        'QuestionWithAllAnswer':'5',
        'QuestionManage': '6',
        'Archive': '6',
        'ModelManage': '7',
        'TempVue': '8',
        'StudentInfo': '9',
        'SystemModelManage': '10',
        'PromptManage': '11',
        'ModelAnswer': '12'
      }[routeName] || '1'
    },
    userName() {
      const studentName = localStorage.getItem('student-info');
      const teacherName = localStorage.getItem('teacher-info');
      const adminName = localStorage.getItem('admin-info');
      return studentName || teacherName || adminName||'游客';
    },
    currentCourseName() {
      console.log('store params:', this.$store.getters['chapter/getCurrentChapterParams']) // 添加调试日志
      return this.$store.getters['chapter/getCurrentChapterParams'].courseName
    },
    currentChapterName() {
      return this.$store.getters['chapter/getCurrentChapterParams'].chapterName
    },
    currentChapterOrder() {
      return this.$store.getters['chapter/getCurrentChapterParams'].chapterOrder
    }

},
methods: {
  handleLogout() {
    localStorage.removeItem('student-token')
    localStorage.removeItem('teacher-token')
    localStorage.removeItem('admin-token')
    localStorage.removeItem('student-info')
    localStorage.removeItem('teacher-info')
    localStorage.removeItem('admin-info')
    localStorage.removeItem('student-id')
    localStorage.removeItem('teacher-id')
    localStorage.removeItem('admin-id')
    this.$router.push('/login')
  },
  // 新增全局刷新方法
    refreshAllProgress() {
      this.$refs.chapterView?.refreshProgress?.()
    },
    setCurrentIds(courseId, chapterId) {
      this.globalState.currentCourseId = courseId
      this.globalState.currentChapterId = chapterId
    },
    // 移除嵌套的methods对象
    async handleMenuSelect(index) {
        if ((index === '3-1' || index === '3-2' || index === '5'||index === '12') && 
            (!this.globalState.currentCourseId || !this.globalState.currentChapterId)) {
            this.$message.warning('请先选择课程和章节');
            this.menuKey += 1;  // 强制菜单重新渲染
            await this.$nextTick();
            return this.$router.push({ name: 'courses' });
        }
        
        let currentProgress = null; // 在方法顶部声明变量
        let questionData = null; // 新增问题数据变量
        // 移除chapterView引用
        if (index === '3-1' || index === '3-2') {
            // 从Vuex获取当前章节参数
          const chapterParams = this.$store.getters['chapter/getCurrentChapterParams'];
                
          // 并行获取进度和问题数据
          const [progressRes, questionRes] = await Promise.all([
            getOrCreateProgress({
              userId: chapterParams.userId,
              courseId: chapterParams.courseId,
              chapterId: chapterParams.chapterId
            }),
            getQuestionByParams({
              courseId: chapterParams.courseId,
              chapterId: chapterParams.chapterId,
              userId: chapterParams.userId
            })
          ]);
          
          if (progressRes.data.code === 1) {
            currentProgress = progressRes.data.data;
            this.$store.commit('chapter/setChapterProgress', [currentProgress]);
          }
          
          if (questionRes.data.code === 1) {
            questionData = questionRes.data.data;
            this.$store.commit('chapter/setQuestionData', questionData);
          }
    
        }
        const routes = {
            '1': { name: 'home' },
            '2': { name: 'courses' },
            '4': { name: 'course-manage' },
            '5': { 
              name: 'CollaborativeImprovement',
              meta: { title: '协同改进' }
            },
            '6': {
              name: 'QuestionManage',
              meta: { title: '问题管理' }
            },
            '7': { 
              name: 'ModelManage',
              meta: { title: '模型管理' }
            },
            '8': {
              name: 'TempVue',
              meta: { title: '多轮对话' }
            },
            '9': {
              name: 'StudentInfo',
              meta: { title: '个人信息' }
            },
            '10': {
              name: 'SystemModelManage',
              meta: { title: '系统模型管理' }
            },
            '11': {
              name: 'PromptManage',
              meta: { title: '系统提示词管理' }
            },
            '12': {
              name: 'ModelAnswer',
              meta: { title: '模范问答' }
            },
            '3-1': () => {
                  if (!this.globalState.currentCourseId || !this.globalState.currentChapterId) {
                      this.$message.warning('请先选择课程和章节')
                      return this.$router.push({ name: 'courses' })
                  }
                  const currentParams = this.$store.getters['chapter/getCurrentChapterParams'];
                  
                  // 确保参数有效性
                  const baseParams = {
                      courseId: this.globalState.currentCourseId.toString(),
                      chapterId: this.globalState.currentChapterId.toString(),
                      question: currentParams?.questionData?.questionContent || '',  // 从questionData获取
                      questionId: currentParams?.questionData?.id?.toString() || ''  // 从questionData获取
                  };
                  console.log('验证参数:', {
                      storeQuestionData: currentParams?.questionData,
                      extractedParams: baseParams
                  });
                  // 参数编码处理
                  // const encodedQuestion = encodeURIComponent(baseParams.question);
                  
                  const routeMap = {
                      0: { 
                          path: `/ask/${baseParams.courseId}/${baseParams.chapterId}`,
                          query: { userId: Number(localStorage.getItem('student-id')) }
                      },
                      1: {
                          path: `/courses/${baseParams.courseId}/chapters/${baseParams.chapterId}`,
                          query: {
                              question: baseParams.question,
                              questionId: baseParams.questionId,
                              userId: Number(localStorage.getItem('student-id'))
                          }
                      },
                      2: {
                        name: 'CompletionPage',  // 新增完成页面路由
                        params: {
                            courseId: baseParams.courseId,
                            chapterId: baseParams.chapterId
                        }
                      },
                      default: {
                          name: 'CompletionPage',  // 默认也指向完成页面
                          params: {
                              courseId: baseParams.courseId,
                              chapterId: baseParams.chapterId
                          }
                      }
                  };

                 
                  const totalProgress = currentProgress ? 
                  (currentProgress.step1Complete + 
                  currentProgress.step2Complete + 
                  currentProgress.step3Complete) : 0;
                  console.log('3-1当前进度:', totalProgress);
                  return routeMap[totalProgress] || routeMap.default;
              },
            '3-2': () => {
                if (!this.globalState.currentCourseId || !this.globalState.currentChapterId) {
                    this.$message.warning('请先选择课程和章节')
                    return this.$router.push({ name: 'courses' })
                }
                // const currentParams = this.$store.getters['chapter/getCurrentChapterParams'];
                const totalProgress = currentProgress ? 
                  (currentProgress.step1Complete + 
                  currentProgress.step2Complete + 
                  currentProgress.step3Complete) : 0;
                  console.log('3-2当前进度:', totalProgress);
            
                const routeMap = {
                
                  2: { 
                    name: 'AnswerSomeone',
                    params: {
                      courseId: this.globalState.currentCourseId.toString(),
                      chapterId: this.globalState.currentChapterId.toString()
                    }
                  },
                  3: {
                    name: 'completionSomeAskPage',  // 指向新页面
                    params: {
                      courseId: this.globalState.currentCourseId.toString(),
                      chapterId: this.globalState.currentChapterId.toString()
                    }
                  }
                };
          
                
                if (totalProgress < 2) {
                  // 新增进度0-1的判断
                    console.log('当前进度:', totalProgress);
                    this.$message.warning('请先完成你问你答步骤');
                    this.menuKey += 1;
                    return this.$router.push({ name: 'courses' });
                }
                return routeMap[totalProgress] || routeMap[2];
                
            }

        }
        const route = typeof routes[index] === 'function' 
            ? routes[index]() 
            : routes[index];
            
        if (route) {
            this.$router.push(route)
        }
    }
}
}
</script>

<style>
/* 新增样式 */
.selection-status {
  color: #fff;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.main-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: auto;
  flex-wrap: nowrap;  /* 新增 */
  overflow: visible; /* 移除外部滚动条 */
}
.main-content {
  margin-left: 0px;
  height: calc(100vh - 60px);
}
.el-container {
  flex: 1;
  min-height: 0; /* 修复flex容器溢出问题 */
}

/* 保留所有有效样式规则 */
.main-header {
  background: linear-gradient(135deg, #409eff 0%, #337ecc 100%);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 30px;
  height: 64px;
}

.system-title {
  font-family: 'Microsoft YaHei', '楷体', cursive;
  font-size: 24px;
  letter-spacing: 1.5px;
  position: relative;
  padding-left: 40px;
}

.system-title::before {
  content: '💡';
  position: absolute;
  left: 0;
  top: -2px;
  font-size: 28px;
}

.selection-status {
  background: rgba(255, 255, 255, 0.15);
  padding: 8px 15px;
  border-radius: 8px;
  backdrop-filter: blur(4px);
}

.logout-link {
  transition: all 0.3s;
  padding: 6px 12px;
  border-radius: 6px;
}

.logout-link:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.system-title {
  font-family: "楷体", cursive;
  font-size: 30px;
  color: #fff;
  margin: 0;
}

.logout-link {
  color: #fff;
  text-decoration: none;
  font-size: 14px;
}


.el-menu-vertical {
  height: calc(100vh - 60px);
  border-right: none;
  background: #f8fafb;
}

.el-menu-item {
  margin: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s;
  color: #606266;
  border: 1px solid #f0f0f0;
}

.el-menu-item:hover {
  background: #ecf5ff;
  color: #409eff;
  border-color: #e0f0ff;
}

.el-menu-item.is-active {
  background: #f0f7ff;
  color: #409eff;
  border-left: 3px solid #409eff;
  font-weight: 500;
}

.menu-divider {
  margin: 16px 0;
  border-color: #e8e8e8;
}

.el-submenu__title {
  border-radius: 8px;
  margin: 8px 12px;
  border: 1px solid transparent;
  transition: all 0.3s;
}

.el-submenu__title:hover {
  border-color: #e0f0ff;
}

.el-submenu .el-menu-item {
  margin-left: 36px !important;
  border: none;
  border-radius: 6px;
}

.menu-divider {
  margin: 12px 0;
  border-color: #e8e8e8;
}

.el-icon {
  margin-right: 8px;
  font-size: 18px;
}



.content-placeholder {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 4px;
  min-height: 400px;
}

.home-bg {
  position: absolute;
  top: 60px;  /* 下移避开顶部导航栏 */
  left: 220px; /* 避开左侧菜单栏 */
  right: 0;
  bottom: 0;
  background-size: cover;
  z-index: 0;
}

.overlay {
  background: rgba(255, 255, 255, 0.85);
  height: calc(100vh - 60px); /* 全屏高度 */
}

/* 修正内容区域层级 */
.content-placeholder {
  position: relative;
  z-index: 1;  /* 移除原来的z-index:-1 */
  height: 80vh;
  background-size: contain;
  background-repeat: no-repeat;
}

.home-bg {
  position: fixed;
  top: 60px;
  left: 220px;
  right: 0;
  bottom: 0;
  z-index: 0;
}
.full-page {
  height: 100vh;
  width: 100vw;
  position: fixed;
  top: 0;
  left: 0;
}
.overlay {
  position: relative;
  z-index: 1;
  min-height: 80vh;
}

/* 增加侧边栏层级 */
.side-menu {
  position: sticky;
  top: 64px;
  left: 0;
  bottom: 0;
  z-index: 1000;
  /* 移除 overflow-y: auto */
  overflow: visible;
  height: calc(100vh - 64px);
}



.home-content {
  max-width: 1200px;
  text-align: center;
}

.welcome-text {
  font-size: 2.5em;
  color: #2c3e50;
  margin-bottom: 40px;
  letter-spacing: 2px;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  margin-bottom: 50px;
}

.stats-card {
  transition: transform 0.3s;
  border-radius: 12px;
}

.stats-card:hover {
  transform: translateY(-5px);
}

.card-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.card-icon {
  font-size: 32px;
  margin-right: 20px;
  color: #409eff;
}

.card-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.card-label {
  color: #909399;
  font-size: 14px;
}

.quick-access {
  margin-top: 30px;
}

.access-btn {
  padding: 18px 36px;
  font-size: 16px;
  margin: 0 15px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}
</style>






