<template>
  <div class="app-container">
    <el-card class="filter-container">
      <el-button type="primary" icon="Plus" @click="handleAdd">入库新饲料</el-button>
    </el-card>

    <el-card class="table-container">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="饲料名称" width="150" />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="当前库存" width="150">
          <template #default="{ row }">
            <span :class="{'low-stock': row.stock < row.minThreshold}">
              {{ row.stock }} {{ row.unit }}
            </span>
            <el-tag v-if="row.stock < row.minThreshold" type="danger" size="small" effect="dark" style="margin-left: 5px">库存预警</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="minThreshold" label="预警阈值" width="120" />
        <el-table-column prop="updateTime" label="更新时间" />
        <el-table-column label="操作" align="center" width="200">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)">调整</el-button>
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
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option label="肉类" value="肉类" />
            <el-option label="蔬果" value="蔬果" />
            <el-option label="干草" value="干草" />
            <el-option label="颗粒" value="颗粒" />
          </el-select>
        </el-form-item>
        <el-form-item label="库存量" prop="stock">
          <el-input-number v-model="form.stock" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="kg, 个, 箱" />
        </el-form-item>
        <el-form-item label="预警阈值" prop="minThreshold">
          <el-input-number v-model="form.minThreshold" :min="0" :precision="2" />
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
import { getFoodList, createFood, updateFood, deleteFood } from '@/api/food'
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
  stock: 0,
  unit: 'kg',
  minThreshold: 10
})
const rules = {
  name: [{ required: true, message: '必填', trigger: 'blur' }],
  type: [{ required: true, message: '必选', trigger: 'change' }]
}

onMounted(() => getList())

const getList = () => {
  loading.value = true
  getFoodList(queryParams).then(res => {
    list.value = res.records
    total.value = res.total
    loading.value = false
  })
}

const resetForm = () => {
  form.id = undefined
  form.name = ''
  form.type = ''
  form.stock = 0
  form.unit = 'kg'
  form.minThreshold = 10
}

const handleAdd = () => {
  resetForm()
  dialog.title = '入库新饲料'
  dialog.visible = true
}

const handleEdit = (row) => {
  resetForm()
  Object.assign(form, row)
  dialog.title = '调整库存'
  dialog.visible = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该饲料吗?', '警告', { type: 'warning' })
      .then(() => {
        deleteFood(row.id).then(() => {
          ElMessage.success('删除成功')
          getList()
        })
      })
}

const submitForm = () => {
  formRef.value.validate(valid => {
    if (valid) {
      if (form.id) {
        updateFood(form.id, form).then(() => {
          ElMessage.success('更新成功')
          dialog.visible = false
          getList()
        })
      } else {
        createFood(form).then(() => {
          ElMessage.success('入库成功')
          dialog.visible = false
          getList()
        })
      }
    }
  })
}
</script>

<style scoped>
.app-container { padding: 20px; }
.low-stock { color: #f56c6c; font-weight: bold; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>