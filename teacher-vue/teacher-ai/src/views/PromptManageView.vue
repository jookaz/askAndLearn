<template>
  <el-card class="box-card">
    <!-- 搜索和新增区域 -->
    <div class="mb-4">
      <el-button type="primary" @click="handleCreate">新增提示词</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData.list"
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="promptName" label="名称" />
      <el-table-column label="内容预览" width="300">
        <template #default="{ row }">
          <el-link type="primary" @click="showDetail(row)">
            {{ truncate(row.promptContent, 100) }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete([row.id])"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      class="mt-4"
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      :total="tableData.total"
      @current-change="fetchData"
    />

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      :title="currentRow.promptName"
      width="60%"
    >
      <el-input
        v-model="currentRow.promptContent"
        type="textarea"
        :autosize="{ minRows: 10, maxRows: 20 }"
        readonly
        class="detail-textarea"
      />
    </el-dialog>

    <!-- 编辑/新增弹窗 -->
    <el-dialog
      v-model="formVisible"
      :title="isEdit ? '编辑提示词' : '新增提示词'"
    >
      <el-form :model="formData" label-width="80px">
        <el-form-item label="名称" required>
          <el-input v-model="formData.promptName" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input
            v-model="formData.promptContent"
            type="textarea"
            :rows="8"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive } from 'vue'
import {
  getPromptPage,
  addPrompt,
  updatePrompt,
  deletePrompts,
  getPromptDetail
} from '@/api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

// 分页数据
const pageNo = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const tableData = reactive({ list: [], total: 0 })

// 详情弹窗控制
const detailVisible = ref(false)
const currentRow = reactive({
  promptName: '',
  promptContent: ''
})

// 表单控制
const formVisible = ref(false)
const isEdit = ref(false)
const formData = reactive({
  id: null,
  promptName: '',
  promptContent: ''
})

// 文本截断方法
const truncate = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.slice(0, length) + '...' : text
}

// 获取数据
const fetchData = async () => {
  try {
    loading.value = true
    const res = await getPromptPage({
      pageNum: pageNo.value,
      pageSize: pageSize.value
    })
    console.log('Fetched data:', res.data)  // 打印返回的数据
    if (res.data.code === 1) {
      console.log('Fetched data111:', res.data)  // 打印返回的数据
      tableData.list = res.data.data.records  // 修改这里
      tableData.total = res.data.data.total
    }
  } catch (e) {
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}

// 显示详情
const showDetail = (row) => {
  currentRow.promptName = row.promptName
  currentRow.promptContent = row.promptContent
  detailVisible.value = true
}

// 删除操作
const handleDelete = async (ids) => {
  try {
    await ElMessageBox.confirm(`确认删除选中的${ids.length}条记录？`)
    const res = await deletePrompts(ids)
    if (res.data.code === 1) { // 添加.data层级
      ElMessage.success('删除成功')
      pageNo.value = 1 // 重置分页
      fetchData()
    }
  } catch (e) {
    /* 取消删除不处理 */
  }
}

// 初始化加载
fetchData()

// 在setup()中添加以下方法
const handleCreate = () => {
  formData.value = {
    id: null,
    promptName: '',
    promptContent: ''
  }
  isEdit.value = false
  formVisible.value = true
}

const handleEdit = async (row) => {
  try {
    const res = await getPromptDetail(row.id)
    if (res.data.code === 1) { // 添加.data层级
      Object.assign(formData, {
        id: row.id,
        promptName: res.data.data.promptName,
        promptContent: res.data.data.promptContent
      })
      isEdit.value = true
      formVisible.value = true
    }
  } catch (e) {
    ElMessage.error('获取详情失败')
  }
}

const submitForm = async () => {
  try {
    const payload = {
      promptName: formData.promptName,
      promptContent: formData.promptContent
    }
    
    let res
    if (isEdit.value) {
      res = await updatePrompt({ ...payload, id: formData.id })
    } else {
      res = await addPrompt(payload)
    }
    
    if (res.data.code === 1) {
      ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
      formVisible.value = false // 确保关闭弹窗
      fetchData()
      Object.assign(formData, { id: null, promptName: '', promptContent: '' })
    }
  } catch (error) {
    ElMessage.error('操作失败：' + (error.message || '未知错误'))
  }
}
</script>

<style scoped>
.detail-textarea {
  font-family: monospace;
  line-height: 1.5;
}
</style>
