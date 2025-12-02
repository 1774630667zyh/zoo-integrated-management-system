<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card class="filter-container">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="动物名称">
          <el-input v-model="queryParams.name" placeholder="输入名称搜索" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
          <el-button type="success" icon="Plus" @click="handleAdd">新增动物</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-container">
      <el-table
          v-loading="loading"
          :data="animalList"
          border
          style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="昵称" width="120" />
        <el-table-column prop="chipCode" label="芯片代码" width="150" />
        <el-table-column prop="speciesId" label="物种" width="120">
          <template #default="{ row }">
            <el-tag>{{ getSpeciesName(row.speciesId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.gender === 1" type="primary">雄</el-tag>
            <el-tag v-else-if="row.gender === 0" type="danger">雌</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="birthDate" label="出生日期" width="120" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">在园</el-tag>
            <el-tag v-else-if="row.status === 2" type="warning">借出</el-tag>
            <el-tag v-else-if="row.status === 3" type="danger">死亡</el-tag>
            <el-tag v-else type="info">出售</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" align="center" min-width="150">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="queryParams.page"
            v-model:page-size="queryParams.size"
            :total="total"
            :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="getList"
            @current-change="getList"
        />
      </div>
    </el-card>

    <!-- 新增/编辑 对话框 -->
    <el-dialog
        :title="dialog.title"
        v-model="dialog.visible"
        width="500px"
        append-to-body
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="昵称" prop="name">
          <el-input v-model="form.name" placeholder="请输入动物昵称" />
        </el-form-item>
        <el-form-item label="物种" prop="speciesId">
          <el-select v-model="form.speciesId" placeholder="请选择物种" style="width: 100%">
            <el-option
                v-for="item in speciesOptions"
                :key="item.id"
                :label="item.commonName"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">雄性</el-radio>
            <el-radio :label="0">雌性</el-radio>
            <el-radio :label="2">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="芯片代码" prop="chipCode">
          <el-input v-model="form.chipCode" placeholder="请输入RFID芯片代码" />
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker
              v-model="form.birthDate"
              type="date"
              placeholder="选择日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="状态">
            <el-option label="在园" :value="1" />
            <el-option label="借出" :value="2" />
            <el-option label="死亡" :value="3" />
            <el-option label="出售" :value="4" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialog.visible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAnimalList, createAnimal, updateAnimal, deleteAnimal } from '@/api/animal'
import { getAllSpecies } from '@/api/species'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const animalList = ref([])
const speciesOptions = ref([])

const queryParams = reactive({
  page: 1,
  size: 10,
  name: ''
})

const dialog = reactive({
  visible: false,
  title: ''
})

const formRef = ref(null)
const form = reactive({
  id: undefined,
  name: '',
  speciesId: undefined,
  gender: 1,
  chipCode: '',
  birthDate: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  speciesId: [{ required: true, message: '请选择物种', trigger: 'change' }],
  chipCode: [{ required: true, message: '请输入芯片代码', trigger: 'blur' }]
}

// 获取物种字典 (为了将 ID 转为 名称显示)
const speciesMap = ref({})

onMounted(() => {
  loadSpecies()
  getList()
})

const loadSpecies = async () => {
  const res = await getAllSpecies()
  if (res) {
    speciesOptions.value = res
    // 构建Map方便列表回显
    res.forEach(item => {
      speciesMap.value[item.id] = item.commonName
    })
  }
}

const getSpeciesName = (id) => {
  return speciesMap.value[id] || id
}

const getList = () => {
  loading.value = true
  getAnimalList(queryParams).then(res => {
    animalList.value = res.records
    total.value = res.total
    loading.value = false
  })
}

const handleQuery = () => {
  queryParams.page = 1
  getList()
}

const resetForm = () => {
  form.id = undefined
  form.name = ''
  form.speciesId = undefined
  form.gender = 1
  form.chipCode = ''
  form.birthDate = ''
  form.status = 1
}

const handleAdd = () => {
  resetForm()
  dialog.title = '新增动物'
  dialog.visible = true
}

const handleEdit = (row) => {
  resetForm()
  Object.assign(form, row)
  dialog.title = '编辑动物'
  dialog.visible = true
}

const submitForm = () => {
  formRef.value.validate(valid => {
    if (valid) {
      if (form.id) {
        updateAnimal(form.id, form).then(() => {
          ElMessage.success('修改成功')
          dialog.visible = false
          getList()
        })
      } else {
        createAnimal(form).then(() => {
          ElMessage.success('新增成功')
          dialog.visible = false
          getList()
        })
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该动物档案吗?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteAnimal(row.id).then(() => {
      ElMessage.success('删除成功')
      getList()
    })
  })
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.filter-container {
  margin-bottom: 20px;
}
.table-container {
  min-height: 500px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>