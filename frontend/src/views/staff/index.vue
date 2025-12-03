<template>
  <div class="app-container">
    <el-card class="filter-container">
      <el-button type="primary" icon="Plus" @click="handleAdd">新增员工</el-button>
    </el-card>

    <el-card class="table-container">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="username" label="工号/用户名" width="150" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.role === 'admin'" type="danger">管理员</el-tag>
            <el-tag v-else-if="row.role === 'vet'" type="success">兽医</el-tag>
            <el-tag v-else-if="row.role === 'feeder'" type="warning">饲养员</el-tag>
            <el-tag v-else>{{ row.role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch
                v-model="row.status"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" align="center" width="150">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)">编辑</el-button>
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
        <el-form-item label="工号" prop="username">
          <el-input v-model="form.username" placeholder="登录账号" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择">
            <el-option label="管理员 (Admin)" value="admin" />
            <el-option label="兽医 (Vet)" value="vet" />
            <el-option label="饲养员 (Feeder)" value="feeder" />
            <el-option label="售票员 (Ticket Seller)" value="ticket_seller" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="初始密码" v-if="!form.id">
          <el-input value="123456" disabled placeholder="默认: 123456" />
          <span style="font-size: 12px; color: #909399; margin-left: 10px;">不可修改</span>
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
import { getStaffList, createStaff, updateStaff } from '@/api/staff'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const queryParams = reactive({ page: 1, size: 10 })

const dialog = reactive({ visible: false, title: '' })
const formRef = ref(null)
const form = reactive({
  id: undefined,
  username: '',
  realName: '',
  role: '',
  phone: '',
  status: 1
})
const rules = {
  username: [{ required: true, message: '必填', trigger: 'blur' }],
  realName: [{ required: true, message: '必填', trigger: 'blur' }],
  role: [{ required: true, message: '必选', trigger: 'change' }]
}

onMounted(() => getList())

const getList = () => {
  loading.value = true
  getStaffList(queryParams).then(res => {
    list.value = res.records
    total.value = res.total
    loading.value = false
  })
}

const resetForm = () => {
  Object.assign(form, {
    id: undefined, username: '', realName: '', role: '', phone: '', status: 1
  })
}

const handleAdd = () => {
  resetForm()
  dialog.title = '新增员工'
  dialog.visible = true
}

const handleEdit = (row) => {
  resetForm()
  Object.assign(form, row)
  dialog.title = '编辑员工信息'
  dialog.visible = true
}

const handleStatusChange = (row) => {
  const text = row.status === 1 ? '启用' : '禁用'
  updateStaff(row.id, { status: row.status }).then(() => {
    ElMessage.success(`账号已${text}`)
  }).catch(() => {
    row.status = row.status === 1 ? 0 : 1 // 恢复状态
  })
}

const submitForm = () => {
  formRef.value.validate(valid => {
    if (valid) {
      const api = form.id ? updateStaff(form.id, form) : createStaff(form)
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