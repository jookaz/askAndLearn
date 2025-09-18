<template>
  <div class="model-manage">
    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleCreate">新建模型</el-button>
      <el-button type="danger" @click="batchDelete">批量删除</el-button>
    </div>

    <!-- 模型表格 -->
    <el-table
      v-loading="loading"
      :data="models"
      border
      stripe
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="modelName" label="模型名称" />
      <el-table-column prop="baseUrl" label="API地址" />
      <el-table-column label="操作" width="200">
        <template #default="{row}">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      v-model:current-page="pageNo"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next"
      :page-sizes="[10, 20, 30]"
      background
    />

    <!-- 模型表单弹窗 -->
    <el-dialog v-model="dialogVisible" title="模型配置">
      <el-form :model="formData" label-width="120px">
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="formData.modelName" />
        </el-form-item>
        <el-form-item label="API地址" prop="baseUrl">
          <el-input v-model="formData.baseUrl" />
        </el-form-item>
        <el-form-item label="API密钥" prop="apiKey">
          <el-input v-model="formData.apiKey" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getModelPage,
  deleteModels,
  createModel,
  updateModel,
  getModelDetail
} from '@/api/model'

// 分页数据
const pageNo = ref(1)
const pageSize = ref(10)
const total = ref(0)
const models = ref([])
const loading = ref(false)

// 表单相关
const dialogVisible = ref(false)
const formData = ref({
  id: null,
  modelName: '',
  baseUrl: '',
  apiKey: ''
})

// 选中项
const selectedIds = ref([])

// 获取模型列表
const fetchModels = async () => {
  loading.value = true
  try {
    const res = await getModelPage({
      pageNo: pageNo.value,
      pageSize: pageSize.value
    })
    models.value = res.data.data.records
    total.value = res.data.data.total
  } catch (error) {
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}

// 监听分页变化
watch([pageNo, pageSize], fetchModels)

// 初始化加载
fetchModels()

// 批量选择
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 删除单个
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该模型？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteModels([id])
    ElMessage.success('删除成功')
    fetchModels()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

// 批量删除
const batchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的模型')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确认删除选中的${selectedIds.value.length}个模型？`, '批量删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteModels(selectedIds.value)
    ElMessage.success('批量删除成功')
    fetchModels()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

// 打开表单
const handleCreate = () => {
  formData.value = { id: null, modelName: '', baseUrl: '', apiKey: '' }
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  try {
    const res = await getModelDetail(row.id)
    formData.value = { ...res.data.data }
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取模型详情失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    let response
    if (formData.value.id) {
      response = await updateModel(formData.value)
    } else {
      response = await createModel(formData.value)
    }
    
    // 根据后端规范检查业务状态码
    if (response.data.code !== 1) {
      throw new Error(response.data.msg || '操作失败')
    }
    
    ElMessage.success(formData.value.id ? '更新成功' : '创建成功')
    dialogVisible.value = false
    fetchModels()
  } catch (error) {
    // 优先显示后端返回的错误信息
    const message = error.response?.data?.msg || error.message
    ElMessage.error(`操作失败：${message}`)
    
    // 特定错误处理（如重复名称）
    if (message.includes('已存在')) {
      formData.value.modelName = ''
    }
  }
}
</script>