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
        <el-table-column prop="speciesId" label="物种" width="120">
          <template #default="{ row }">
            <el-tag>{{ getSpeciesName(row.speciesId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">在园</el-tag>
            <el-tag v-else-if="row.status === 2" type="warning">借出</el-tag>
            <el-tag v-else-if="row.status === 3" type="danger">死亡</el-tag>
            <el-tag v-else type="info">出售</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" align="center" min-width="250">
          <template #default="{ row }">
            <el-button type="primary" link icon="FirstAidKit" @click="handleMedical(row)">病历</el-button>
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

    <!-- 新增/编辑 动物对话框 -->
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

    <!-- 医疗记录 抽屉 (Drawer) -->
    <el-drawer
        v-model="medicalDrawer.visible"
        :title="medicalDrawer.title"
        size="50%"
        destroy-on-close
    >
      <div style="padding: 0 20px;">
        <!-- 新增病历表单 -->
        <el-card class="box-card" shadow="never" style="margin-bottom: 20px;">
          <template #header>
            <div class="card-header">
              <span>录入新就诊记录</span>
            </div>
          </template>
          <el-form :model="medicalForm" label-width="80px">
            <el-form-item label="症状">
              <el-input v-model="medicalForm.symptoms" placeholder="描述症状" />
            </el-form-item>
            <el-form-item label="诊断">
              <el-input v-model="medicalForm.diagnosis" placeholder="诊断结果" />
            </el-form-item>
            <el-form-item label="治疗">
              <el-input v-model="medicalForm.treatment" type="textarea" placeholder="治疗方案及用药" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitMedical">保存记录</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 历史记录时间轴 -->
        <h3>历史诊疗记录</h3>
        <el-timeline v-if="medicalHistory.length > 0">
          <el-timeline-item
              v-for="(record, index) in medicalHistory"
              :key="index"
              :timestamp="record.visitDate"
              placement="top"
              type="primary"
          >
            <el-card>
              <h4>诊断: {{ record.diagnosis }}</h4>
              <p><strong>症状:</strong> {{ record.symptoms }}</p>
              <p><strong>治疗:</strong> {{ record.treatment }}</p>
              <p style="color: #909399; font-size: 12px;">兽医ID: {{ record.vetId }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无医疗记录" />
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAnimalList, createAnimal, updateAnimal, deleteAnimal } from '@/api/animal'
import { getAllSpecies } from '@/api/species'
import { getMedicalHistory, createMedicalRecord } from '@/api/medical'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const animalList = ref([])
const speciesOptions = ref([])
const speciesMap = ref({})

// 用户Store，用于获取当前兽医ID
const userStore = useUserStore()

// 查询参数
const queryParams = reactive({
  page: 1,
  size: 10,
  name: ''
})

// 动物弹窗
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

// 医疗相关
const medicalDrawer = reactive({
  visible: false,
  title: '',
  animalId: null
})
const medicalHistory = ref([])
const medicalForm = reactive({
  symptoms: '',
  diagnosis: '',
  treatment: ''
})

onMounted(() => {
  loadSpecies()
  getList()
})

// --- 基础数据与列表逻辑 ---

const loadSpecies = async () => {
  const res = await getAllSpecies()
  if (res) {
    speciesOptions.value = res
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

// --- 动物增删改逻辑 ---

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

// --- 医疗记录逻辑 ---

const handleMedical = (row) => {
  medicalDrawer.animalId = row.id
  medicalDrawer.title = `医疗记录 - ${row.name}`
  medicalDrawer.visible = true

  // 重置表单
  medicalForm.symptoms = ''
  medicalForm.diagnosis = ''
  medicalForm.treatment = ''

  loadMedicalHistory(row.id)
}

const loadMedicalHistory = (animalId) => {
  getMedicalHistory(animalId).then(res => {
    medicalHistory.value = res.records || []
  })
}

const submitMedical = () => {
  if (!medicalForm.diagnosis) {
    ElMessage.warning('请填写诊断结果')
    return
  }

  const data = {
    animalId: medicalDrawer.animalId,
    // 假设当前登录用户ID为兽医ID，实际应从Token解析
    vetId: 1, // 这里暂时硬编码，或者从 userStore 获取
    symptoms: medicalForm.symptoms,
    diagnosis: medicalForm.diagnosis,
    treatment: medicalForm.treatment
  }

  createMedicalRecord(data).then(() => {
    ElMessage.success('病历保存成功')
    // 刷新列表并清空表单
    loadMedicalHistory(medicalDrawer.animalId)
    medicalForm.symptoms = ''
    medicalForm.diagnosis = ''
    medicalForm.treatment = ''
  })
}
</script>

<style scoped>
.app-container { padding: 20px; }
.filter-container { margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>