<template>
  <!-- 学生首页 -->
  <div v-if="!isTeacher" class="student-container">
    <h1 class="welcome-title">欢迎来到智能问答学习平台！</h1>
    <p class="system-intro">
      本系统为您提供结构化学习路径，通过以下三个阶段帮助您深度掌握知识：
    </p>
    
    <el-steps direction="vertical" :active="progressStatus">
      <el-step title="第一阶段：课程选择（必选）">
        <template #description>
          <div class="step-desc">
            <h4>在正式学习前，请先完成课程选择</h4>
            <p>· 浏览现有课程目录，选择符合您学习目标的课程</p>
            <p>· 课程选定后不可更改，请谨慎选择</p>
            <p>· 选课完成后自动解锁后续功能</p>
          </div>
        </template>
      </el-step>

      <el-step title="第二阶段：自主问答（必选）">
        <template #description>
          <div class="step-desc">
            <h4>与AI导师进行深度问答交互</h4>
            <p>· 提出课程相关问题，获取即时解答</p>
            <p>· 支持多轮追问和上下文关联问答</p>
            <p>· 问答记录将自动归档供复习使用</p>
          </div>
        </template>
      </el-step>

      <el-step title="第三阶段：协同改进（可选）">
        <template #description>
          <div class="step-desc">
            <h4>参与学习社区的知识共建</h4>
            <p>· 查看其他同学提出的优质问题</p>
            <p>· 对现有答案进行补充或提出不同见解</p>
            <p>· 协作改进后的答案将共享给全体学员</p>
          </div>
        </template>
      </el-step>
    </el-steps>
  </div>

  <!-- 教师首页 -->
  <div v-else class="teacher-container">
    <div class="teacher-header">
      <h1 class="welcome-title">教学管理中心</h1>
      <p class="welcome-sub">
        当前学年：{{ currentYear - 1 }}-{{ currentYear }}学年 | 
        已管理课程：{{ courseCount || 0 }}门
      </p>
    </div>
    
    <div class="grid-container">
      <el-card 
        v-for="item in teacherModules" 
        :key="item.path"
        class="grid-item"
        shadow="hover">
        <component :is="item.icon" class="module-icon"/>
        <h3>{{ item.title }}</h3>
        <p class="module-desc">{{ item.description }}</p>
        <el-button 
          type="primary" 
          class="enter-btn"
          @click="$router.push(item.path)">
          进入管理 →
        </el-button>
      </el-card>
    </div>
  </div>
</template>

<style>
/* 优化教师端网格布局 */
.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  padding: 0 40px;
}

.grid-item {
  transition: transform 0.3s;
  cursor: pointer;
  min-height: 180px;
}

.grid-item:hover {
  transform: translateY(-5px);
}

.enter-btn {
  margin-top: 15px;
}

/* 增强学生端文字说明 */
.system-intro {
  font-size: 16px;
  color: #666;
  line-height: 1.8;
  margin-bottom: 30px;
}

.step-desc {
  padding: 10px 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.step-desc h4 {
  color: #2c3e50;
  margin-bottom: 10px;
}

.step-desc p {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin: 8px 0;
}
</style>

<script>
import { FolderOpened, List, Cpu } from '@element-plus/icons-vue'
import { getCourseCount } from '@/api/course'

export default {
  components: { FolderOpened, List, Cpu },
  computed: {
    isStudent() {
      return !!localStorage.getItem('student-id')
    },
    isTeacher() {
      return !!localStorage.getItem('teacher-id')
    },
    teacherModules() {
      return [
        {
          path: '/course-manage',
          icon: 'FolderOpened',
          title: '课程管理',
          description: '管理课程体系与章节内容'
        },
        {
          path: '/teacher/questions',
          icon: 'List',
          title: '问题管理',
          description: '查看并管理学生提出的所有问题'
        },
        {
          path: '/model-manage',
          icon: 'Cpu',
          title: '模型管理',
          description: '配置和维护AI问答模型参数'
        }
      ]
    }
  },
  data() {
    return {
      courseCount: 0,
      currentYear: new Date().getFullYear()
    }
  },
  mounted() {
    if (this.isTeacher) {
      this.fetchCourseCount()
    }
  },
  methods: {
    async fetchCourseCount() {
      try {
        const { data } = await getCourseCount()
        this.courseCount = data.data
      } catch (error) {
        console.error('接口调用失败', error)
        this.$message.error('课程数量获取失败')
        this.courseCount = 0
      }
    }
  }
}
</script>

<style>
/* 新增学生端样式 */
.student-container {
  padding: 40px;
  max-width: 800px;
  margin: 0 auto;
}

.welcome-title {
  color: #2c3e50;
  margin-bottom: 40px;
  text-align: center;
}

.disabled-step {
  color: #909399;
  cursor: not-allowed;
}

/* 优化教师端样式 */
.teacher-container {
  padding: 40px;
}

.welcome-sub {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.module-desc {
  color: #666;
  font-size: 14px;
  margin-top: 10px;
}
</style>
