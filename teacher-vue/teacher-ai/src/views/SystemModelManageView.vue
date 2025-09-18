<template>
  <div class="model-manage-container">
    <!-- 操作工具栏 -->
    <div class="operation-bar">
      <el-button type="primary" @click="openCreateDialog">新建模型</el-button>
      <el-button type="danger" @click="batchDelete">批量删除</el-button>
    </div>

    <!-- 模型表格 -->
    <el-table
      v-loading="loading"
      :data="modelList"
      border
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="modelName" label="模型名称" min-width="120" />
      <el-table-column prop="baseUrl" label="API地址" min-width="200" />
      <!-- 新增系统模型状态列 -->
      <el-table-column label="系统模型" width="120">
        <template #default="{ row }">
          {{ row.systemFlag === 1 ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
          <el-button size="small" @click="openEditDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          <!-- 新增设置系统模型按钮 -->
          <el-button 
            size="small" 
            :type="row.systemFlag === 1 ? 'warning' : 'success'"
            @click="handleSetSystemModel(row)"
          >
            {{ row.systemFlag === 1 ? '系统模型' : '设为系统模型' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.size"
      :total="pagination.total"
      :page-sizes="[10, 20, 30]"
      layout="total, sizes, prev, pager, next"
      class="pagination-wrapper"
    />

    <!-- 模型配置弹窗 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="模型名称" required>
          <el-input v-model="formData.modelName" />
        </el-form-item>
        <el-form-item label="API地址" required>
          <el-input v-model="formData.baseUrl" />
        </el-form-item>
        <el-form-item label="API密钥">
          <el-input v-model="formData.apiKey" type="password" show-password />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getModelPage,
  deleteModels,
  createModel,
  updateModel,
  getModelDetail,
  setSystemModel // 需要添加新的API方法
} from '@/api/admin'

// 新增设置系统模型处理方法
const handleSetSystemModel = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确认${row.systemFlag === 1 ? '取消' : '设置'}该系统模型？`,
      '操作确认', 
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const { data } = await setSystemModel(row.id)
    if (data.code === 1) {
      ElMessage.success('操作成功')
      // 刷新列表或局部更新数据
      fetchModels()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.response?.data?.message || '操作失败')
    }
  }
}

// 分页数据
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 模型数据
const modelList = ref([])
const loading = ref(false)
const selectedIds = ref([])

// 弹窗控制
const dialog = reactive({
  visible: false,
  title: '新建模型',
  isEdit: false
})

// 表单数据
const formData = reactive({
  id: null,
  modelName: '',
  baseUrl: '',
  apiKey: ''
})

// 获取模型列表
const fetchModels = async () => {
  loading.value = true
  try {
    const { data } = await getModelPage({
      pageNo: pagination.page,
      pageSize: pagination.size
    })
    
    if (data.code === 1) {
      modelList.value = data.data.records
      pagination.total = data.data.total
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '数据加载失败')
  } finally {
    loading.value = false
  }
}

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
// 打开创建弹窗
const openCreateDialog = () => {
  Object.assign(formData, {
    id: null,
    modelName: '',
    baseUrl: '',
    apiKey: ''
  })
  dialog.title = '新建模型'
  dialog.isEdit = false
  dialog.visible = true
}

// 打开编辑弹窗
const openEditDialog = async (row) => {
  try {
    const { data } = await getModelDetail(row.id)
    if (data.code === 1) {
      Object.assign(formData, data.data)
      dialog.title = '编辑模型'
      dialog.isEdit = true
      dialog.visible = true
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '获取详情失败')
  }
}

// 提交表单
const submitForm = async () => {
  try {
    let res
    if (dialog.isEdit) {
      res = await updateModel(formData)
    } else {
      res = await createModel(formData)
    }

    if (res.data.code === 1) {
      ElMessage.success(dialog.isEdit ? '更新成功' : '创建成功')
      dialog.visible = false
      fetchModels()
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  }
}

// 初始化加载
onMounted(fetchModels)
</script>

<style scoped>
.model-manage-container {
  padding: 20px;
}
.operation-bar {
  margin-bottom: 20px;
}
.pagination-wrapper {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>