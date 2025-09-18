import { createRouter, createWebHistory } from 'vue-router'
// 移除未使用的HomeView和CourseView导入
import ChapterView from '../views/ChapterView.vue'
import store from '@/store' // 新增store导入
import LoginView from '@/views/LoginView.vue'
import ArchiveView from '@/views/ArchiveView.vue'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { isFullPage: true } // 新增标记
  },
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView.vue'), // 恢复首页组件
    meta: { requiresAuth: false }
  },
  {
    path: '/courses',
    name: 'courses',
    component: () => import('@/views/CourseView.vue'), // 修正组件引用
    meta: { requiresAuth: true }
  },
  {
    path: '/courses/:courseId/chapters',
    name: 'chapters',
    component: ChapterView,
    meta: { requiresAuth: true }  // 添加认证要求
  },
  {
    path: '/courses/:courseId/chapters/:chapterId',
    name: 'chapter-detail',
    component: () => import('@/views/ChapterDetailView.vue'),
    meta: { requiresAuth: true }  // 添加认证要求
  },
  {
    path: '/course-manage',
    name: 'course-manage',
    component: () => import('@/views/CourseManageView.vue'),
    meta: { requiresAuth: true }  // 添加路由守卫配置
  },
  {
    path: '/ask/:courseId/:chapterId',
    name: 'ask',
    component: () => import('@/views/AskView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/improve-answer/:courseId/:chapterId',
    name: 'ImproveAnswer',
    component: () => import('@/views/ImproveAnswerView.vue'),
    props: true
  },
  {
    path: '/qa-self',
    name: 'qa-self',
    component: () => import('@/views/QASelfView.vue')
  },
  {
    path: '/answer-someone/:courseId/:chapterId',
    name: 'AnswerSomeone',
    component: () => import('../views/AnswerSomeoneView.vue'),
    props: true // 启用props接收参数
  },
  {
    path: '/question-detail/:questionId',
    name: 'QuestionDetail',
    component: () => import('@/views/QuestionDetailView.vue'),
    props: true
  },
  {
    path: '/question-with-answers/:questionId',
    name: 'QuestionWithAllAnswer',
    component: () => import('@/views/QuestionWithAllAnswer.vue'),
    props: true
  },
  {
    path: '/completion/:courseId/:chapterId',
    name: 'CompletionPage',
    component: () => import('@/views/CompletionPage.vue'),
    props: true
  },
  {
    path: '/completion-ask/:courseId/:chapterId',
    name: 'completionSomeAskPage',
    component: () => import('@/views/completionSomeAskPage.vue'),
    props: true
  },
  {
    path: '/collaborative',
    name: 'CollaborativeImprovement',
    component: () => import('@/views/CollaborativeImprovement.vue')
  },
  {
    path: '/teacher/questions',
    name: 'QuestionManage',
    component: () => import('@/views/QuestionManageView.vue'),
    meta: { 
      title: '问题管理',
      roles: ['teacher'],
      isFullPage: false
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { isFullPage: true } // 明确设置认证要求
  },
  {
    path: '/model-manage',
    name: 'ModelManage',
    component: () => import('@/views/ModelManageView.vue'),
    meta: { requiresAuth: true, isTeacher: true }
  },
  {
    path: '/archive',
    name: 'Archive',
    component: ArchiveView,
    props: (route) => ({ questionId: route.query.questionId })
  },
  {
    path: '/temp-vue',
    name: 'TempVue',
    component: () => import('@/views/TempVue.vue'),
    meta: { 
      requiresAuth: true,
      roles: ['student'],
      // isFullPage: true
    }
  },
  {
    path: '/student-info',
    name: 'StudentInfo',
    component: () => import('../views/StudentVue.vue'),
    meta: {
      requireAuth: true,
      roles: ['student'],
    }
  },
  {
    path: '/system-model',
    name: 'SystemModelManage',
    component: () => import('@/views/SystemModelManageView.vue'),
    meta: {
      requiresAuth: true,
      title: '系统模型管理',
      roles: ['admin'],
    }
  },
  {
    path: '/prompt-manage',
    name: 'PromptManage',
    component: () => import('@/views/PromptManageView.vue'),
    meta: {
      requiresAuth: true,
      title: '提示词管理',
      roles: ['admin']
    }
  },
  // 在studentRoutes中添加
  {
    path: '/model-answer',
    name: 'ModelAnswer',
    component: () => import('@/views/ModelAnswerView.vue'),
    meta: { 
      requiresAuth: true,
      roles: ['student']
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), // Changed from import.meta.env
  routes
})


// 新增登录验证逻辑
router.beforeEach((to, from, next) => {
  const studentToken = localStorage.getItem('student-token')
  const teacherToken = localStorage.getItem('teacher-token')
  const adminToken = localStorage.getItem('admin-token')
  
  // 允许访问注册页
  if (to.name === 'Register' || to.name === 'login') {
    return next()
  }
  
  if (!studentToken && !teacherToken&& !adminToken) {
    next({ name: 'login' })
  } else {
    next()
  }
})
router.beforeEach(async (to) => {
  const userId = store.state.user?.id
  const courseId = to.params.courseId || store.getters['chapter/getCurrentChapterParams']?.courseId
  
  if (userId && courseId) {
    await store.dispatch('chapter/fetchAllChapterProgress', { 
      userId,
      courseId
    })
  }
  return true
})

export default router

