<template>
  <div class="app-container">
    <el-card class="filter-container">
      <el-button type="primary" icon="Plus" @click="handleAdd">新增场馆</el-button>
    </el-card>

    <el-card class="table-container">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="场馆名称" width="150" />
        <el-table-column prop="type" label="类型" width="100" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="capacity" label="容纳人数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">开放中</el-tag>
            <el-tag v-else-if="row.status === 2" type="warning" effect="dark">维护中</el-tag>
            <el-tag v-else type="info">已关闭</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
            v-model:current-page="queryParams.page"
            v-model:page-size="queryParams.size"
            :total="total"
            :page-sizes="[10, 20]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="getList"
            @current-change="getList"
        />
      </div>
    </el-card>

    <!-- 弹窗 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="例如：大熊猫馆" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option label="室内" value="室内" />
            <el-option label="室外" value="室外" />
            <el-option label="游客服务" value="游客服务" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="form.location" placeholder="例如：园区A区" />
        </el-form-item>
        <el-form-item label="最大容量" prop="capacity">
          <el-input-number v-model="form.capacity" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">开放</el-radio>
            <el-radio :label="0">关闭</el-radio>
            <el-radio :label="2">维护</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getFacilityList, createFacility, updateFacility, deleteFacility } from '@/api/facility'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const queryParams = reactive({ page: 1, size: 10 })

const dialog = reactive({ visible: false, title: '' })
const formRef = ref(null)
const form = reactive({
  id: undefined,
  name: '',
  type: '',
  location: '',
  capacity: 0,
  status: 1
})
const rules = {
  name: [{ required: true, message: '必填', trigger: 'blur' }],
  type: [{ required: true, message: '必选', trigger: 'change' }]
}

onMounted(() => getList())

const getList = () => {
  loading.value = true
  getFacilityList(queryParams).then(res => {
    list.value = res.records
    total.value = res.total
    loading.value = false
  })
}

const resetForm = () => {
  Object.assign(form, {
    id: undefined, name: '', type: '', location: '', capacity: 0, status: 1
  })
}

const handleAdd = () => {
  resetForm()
  dialog.title = '新增场馆'
  dialog.visible = true
}

const handleEdit = (row) => {
  resetForm()
  Object.assign(form, row)
  dialog.title = '编辑场馆'
  dialog.visible = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除?', '警告', { type: 'warning' }).then(() => {
    deleteFacility(row.id).then(() => {
      ElMessage.success('删除成功')
      getList()
    })
  })
}

const submitForm = () => {
  formRef.value.validate(valid => {
    if (valid) {
      const api = form.id ? updateFacility(form.id, form) : createFacility(form)
      api.then(() => {
        ElMessage.success('操作成功')
        dialog.visible = false
        getList()
      })
    }
  })
}
</script>

<style scoped>
.app-container { padding: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>