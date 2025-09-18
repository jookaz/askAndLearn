<template>
  <div class="question-manage">
    <el-form :model="queryParams" inline class="search-form">
      <el-row :gutter="15" style="width:100%">
        <!-- 第一行 -->
        <el-col :span="8">
          <el-form-item label="所属课程" style="width:100%">
            <el-select v-model="queryParams.courseId" clearable style="width:100%">
              <el-option
                v-for="item in courseOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        
        <el-col :span="8">
          <el-form-item label="所属章节" style="width:100%">
            <el-select 
              v-model="queryParams.chapterId" 
              clearable 
              style="width:100%"
              :disabled="!queryParams.courseId"
            >
              <el-option
                v-for="item in chapterOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
    
        <el-col :span="8">
          <el-form-item label="认知层次" style="width:100%">
            <el-select v-model="queryParams.cognitiveLevel" clearable style="width:100%">
              <el-option
                v-for="item in cognitiveLevels"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    
      <!-- 第二行 -->
      <el-row :gutter="15" style="width:100%;margin-top:10px">
        <el-col :span="12">
          <el-form-item label="问题内容" style="width:100%">
            <el-input
              v-model="queryParams.questionContent"
              placeholder="输入问题关键词"
              clearable
              style="width:100%"
            />
          </el-form-item>
        </el-col>
    
        <el-col :span="12">
          <el-form-item label="排序方式" style="width:100%;display:flex;align-items:center">
            <el-select 
              v-model="queryParams.sort" 
              clearable
              placeholder="排序字段"
              style="flex:1;margin-right:10px"
            >
              <el-option
                v-for="item in sortOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-select 
              v-model="queryParams.isAsc" 
              clearable
              placeholder="排序顺序"
              style="flex:1"
            >
              <el-option label="升序" :value="true" />
              <el-option label="降序" :value="false" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="resetQuery">重置</el-button>
    </el-form>
    <el-dialog v-model="answerDialogVisible" title="问题详情及回答" width="70%">
            <div v-if="currentQuestion">
              <h3>{{ currentQuestion.questionContent }}</h3>
              <el-descriptions :column="2" border> 
                <el-descriptions-item label="提问学生">{{ currentQuestion.studentName }}</el-descriptions-item>
                <el-descriptions-item label="所在班级">{{ currentQuestion.classes }}</el-descriptions-item>
                <el-descriptions-item label="认知层次">{{ currentQuestion.cognitiveLevel }}</el-descriptions-item>
                <el-descriptions-item label="总点赞数">{{ currentQuestion.likeNumber }}</el-descriptions-item>
              </el-descriptions>
                <!-- 提出者回答 -->
                <el-card header="提出者回答" class="answer-section">
                  <el-table 
                  :data="proposerAnswers"
                  empty-text="暂无回答"
                  :disabled="!isQuestionSelected"
                   @selection-change="isQuestionSelected && 
                   handleAnswerSelection('proposer', $event)"
                  border>
                    <!-- <el-table-column type="selection" width="55"
                    :selectable="()=>isQuestionSelected" /> -->
                    <el-table-column prop="content" label="回答内容" min-width="300">
                      <template #default="{ row }">
                        <div v-html="renderMarkdown(row.content)"></div>
                      </template>
                    </el-table-column>
                    <el-table-column prop="studentName" label="学生姓名" width="100" />
                    <el-table-column prop="classes" label="学生班级" width="100" />
                    <el-table-column prop="likeNumber" label="点赞数" width="100" />
                    <el-table-column prop="submitTime" label="提交时间" width="180" />
                  </el-table>
                </el-card>

                <!-- 改进回答 -->
                <el-card header="改进回答" class="answer-section">
                  <el-table 
                  :data="improveAnswers"
                  empty-text="暂无回答"
                  :disabled="!isQuestionSelected" 
                  @selection-change="isQuestionSelected && 
                  handleAnswerSelection('improve', $event)"
                  border>
                    <!-- <el-table-column type="selection" width="55" 
                    :selectable="()=>isQuestionSelected"/> -->
                    <el-table-column prop="content" label="回答内容" min-width="300">
                      <template #default="{ row }">
                        <div v-html="renderMarkdown(row.content)"></div>
                      </template>
                    </el-table-column>
                    <el-table-column prop="studentName" label="学生姓名" width="100" />
                    <el-table-column prop="classes" label="学生班级" width="100" />
                    <el-table-column prop="likeNumber" label="点赞数" width="100" />
                    <el-table-column prop="submitTime" label="提交时间" width="180" />
                  </el-table>
                </el-card>

                <!-- 其他回答 -->
                <el-card header="其他回答" class="answer-section">
                  <el-table 
                  :data="otherAnswers"
                  empty-text="暂无回答"
                  :disabled="!isQuestionSelected" 
                  @selection-change="isQuestionSelected &&
                  handleAnswerSelection('other', $event)"
                  border>
                    <!-- <el-table-column type="selection" width="55" 
                    :selectable="()=>isQuestionSelected"/> -->
                    <el-table-column prop="content" label="回答内容" min-width="300">
                      <template #default="{ row }">
                        <div v-html="renderMarkdown(row.content)"></div>
                      </template>
                    </el-table-column>
                    <el-table-column prop="studentName" label="学生姓名" width="100" />
                    <el-table-column prop="classes" label="学生班级" width="100" />
                    <el-table-column prop="likeNumber" label="点赞数" width="100" />
                    <el-table-column prop="submitTime" label="提交时间" width="180" />
                  </el-table>
                  <!-- 在对话框底部添加提示 -->
                  <!-- <template #footer>
                    <span v-if="!isQuestionSelected" style="color: #F56C6C;margin-right: 20px">请先在问题列表中勾选该问题，才能选择答案</span>
                    <el-button @click="answerDialogVisible = false">关闭</el-button>
                  </template> -->
                </el-card>
            </div>
    </el-dialog>
    <el-table :data="tableData" border @selection-change="handleSelectionChange">
      <!-- 删除勾选框列 -->
      <!-- <el-table-column type="selection" width="55" /> -->
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="questionContent" label="问题内容" min-width="200">
        <template #default="{ row }">
          <span class="clickable-text" @click="showAnswerDialog(row)">{{ row.questionContent }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="courseName" label="课程名称" min-width="150" />
      <el-table-column prop="chapterName" label="章节名称" min-width="150" />
      <el-table-column prop="cognitiveLevel" label="认知层次" width="120" />
      <el-table-column prop="likeNumber" label="点赞数" width="100" />
      <el-table-column prop="createTime" label="提交时间" width="180"/>
      <!-- 新增归档按钮列 -->
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button 
            type="primary"
            @click="toArchivePage(scope.row)"
          >
            进行归档
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="pagination.pageNo"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
      layout="total, sizes, prev, pager, next, jumper"
      class="pagination"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted ,watch,computed} from 'vue'
import { getQuestionPage,getCourseNames ,getChapterNames,getAllAnswer} from '@/api/teacher'
import { ElMessage} from 'element-plus'
import { marked } from 'marked';
import DOMPurify from 'dompurify';
import { useRouter } from 'vue-router' 

const router = useRouter() // 在setup顶部添加
// 新增状态管理
const selectedQuestions = ref([])

const isQuestionSelected = computed(() => {
  return selectedQuestions.value.some(q => q.id === currentQuestion.value?.id)
})
// 答案选择处理
const handleAnswerSelection = (type, selection) => {
  if (!isQuestionSelected.value) {
    console.log(isQuestionSelected.value)
    ElMessage.warning('请先在问题列表中勾选该问题')
    return
  }
  const question = selectedQuestions.value.find(
    q => q.id === currentQuestion.value.id
  );
  
  if (question) {
    question.selectedAnswers = {
      ...question.selectedAnswers,
      [type]: selection.map(a => ({
        id: a.id,
        content: a.content,
        questionId: currentQuestion.value.id
      }))
    };
  }
};

// 初始化问题选择状态
const handleSelectionChange = (selection) => {
  selectedQuestions.value = selection.map(q => ({
    ...q,
    selectedAnswers: {
      proposer: [],
      improve: [],
      other: []
    }
  }));
};

// 归档数据组装
const toArchivePage = (row) => {
  router.push({
    path: '/archive',
    query: { 
      questionId: row.id // 确保使用正确的参数名
    }
  })
}
const renderMarkdown = (content) => {
  return DOMPurify.sanitize(marked.parse(content || ''))
}
// 在script部分添加
const answerDialogVisible = ref(false)
const currentQuestion = ref(null)
// 添加重置方法
const resetQuery = () => {
  Object.keys(queryParams).forEach(key => {
    queryParams[key] = null
  })
  pagination.pageNo = 1
  fetchData()
}
// 响应式数据定义
const proposerAnswers = ref([])
const improveAnswers = ref([])
const otherAnswers = ref([])

const showAnswerDialog = async (row) => {
  try {
    // // 新增校验：必须先在问题列表中选中该问题
    // if (!selectedQuestions.value.some(q => q.id === row.id)) {
    //   ElMessage.warning('请先在问题列表中勾选该问题')
    //   return
    // }
    currentQuestion.value = row
    
    const res = await getAllAnswer(row.id)
    currentQuestion.value.studentName = res.data.data.studentName
    currentQuestion.value.classes = res.data.data.classes
    // 重组三种回答数据
    // 提出者回答（仅当answerId存在时初始化）
    proposerAnswers.value = res.data.data.answerId ? [{
      id: res.data.data.answerId,
      content: res.data.data.answerContent,
      likeNumber: res.data.data.answerLikeNumber,
      submitTime: res.data.data.answerSubmitTime,
      studentName: res.data.data.answerStudentName,
      classes: res.data.data.answerStudentClasses
    }] : []

    // 改进回答（仅当improveAnswerId存在时初始化）
    improveAnswers.value = res.data.data.improveAnswerId ? [{
      id: res.data.data.improveAnswerId,
      content: res.data.data.improveAnswerContent,
      likeNumber: res.data.data.improveAnswerLikeNumber,
      submitTime: res.data.data.improveAnswerSubmitTime,
      studentName: res.data.data.improveAnswerStudentName,
      classes: res.data.data.improveAnswerStudentClasses
    }] : []

    otherAnswers.value = res.data.data.studentAnswerAllVOList?.map(item => ({
      id: item.id,
      content: item.answerContent,
      likeNumber: item.likeNumber,
      submitTime: new Date(item.submitTime).toLocaleString(),
      studentName: item.studentName,
      classes: item.classes
    })) || []
    
    answerDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取回答数据失败')
  }
}

// 初始化查询参数
const queryParams = reactive({
  courseId: null,
  chapterId: null,
  cognitiveLevel: null,
  questionContent: '',
  sort: null,
  isAsc: null
})

// 分页参数
const pagination = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 选项数据
const courseOptions = ref([])
const chapterOptions = ref([])
// 选项数据（解除注释并修改格式）
const cognitiveLevels = [
  { value: '记忆', label: '记忆' },
  { value: '理解', label: '理解' },
  { value: '应用', label: '应用' },
  { value: '分析', label: '分析' },
  { value: '评价', label: '评价' },
  { value: '综合', label: '综合' }
]

// 监听课程变化
watch(() => queryParams.courseId, async (newVal) => {
  queryParams.chapterId = null // 清空已选章节
  chapterOptions.value = []
  
  if (newVal) {
    try {
      const res = await getChapterNames(newVal)
      chapterOptions.value = res.data.data.map(item => {
        const [label, value] = Object.entries(item)[0]
        return { label, value } // 确保返回正确的格式，包括 label 和 valu
      })
    } catch (error) {
      console.error('获取章节失败:', error)
    }
  }
})
const sortOptions = [
  { value: 'like_number', label: '点赞数' },
  { value: 'create_time', label: '创建时间' }
]

// 获取数据
const fetchData = async () => {
  try {
    const params = {
      ...queryParams,
      ...pagination
    }
    // console.log('请求参数:', params)
    const res = await getQuestionPage(params)
    // console.log('接口响应:', res)
    tableData.value = res.data.data.records
    pagination.total = res.data.data.total
   
   
  } catch (error) {
    console.error('获取数据失败:', error)
  }
}


// 修改分页处理
const handlePageChange = (page) => {
  pagination.pageNo = page
  fetchData()
}

// 处理每页条数变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchData()
}

// 处理搜索
const handleSearch = () => {
  pagination.pageNo = 1
  fetchData()
}

onMounted(async () => {
  try {
    const res = await getCourseNames()
    courseOptions.value = res.data.data.map(item => {
      const [label, value] = Object.entries(item)[0]
      return { label, value }
    })
  } catch (error) {
    console.error('获取课程列表失败:', error)
  }
  fetchData()
})
</script>

<style scoped>

.clickable-text {
  cursor: pointer;
  color: #409EFF;
}
.question-manage {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
.search-form .el-form-item {
  margin-bottom: 10px;
}
.search-form .el-select {
  min-width: 200px;
}
.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
