<template>
  <div class="app-container">
    <el-card class="filter-container">
      <el-button type="primary" icon="Plus" @click="handleAdd">新增物种</el-button>
    </el-card>

    <el-card class="table-container">
      <el-table :data="list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="commonName" label="中文俗名" width="150" />
        <el-table-column prop="scientificName" label="学名" />
        <el-table-column prop="conservationStatus" label="保护等级" width="120">
          <template #default="{ row }">
            <el-tag v-if="['EN', 'CR'].includes(row.conservationStatus)" type="danger">{{ row.conservationStatus }}</el-tag>
            <el-tag v-else-if="row.conservationStatus === 'VU'" type="warning">{{ row.conservationStatus }}</el-tag>
            <el-tag v-else type="success">{{ row.conservationStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dietType" label="食性" width="120" />
      </el-table>
    </el-card>

    <!-- 新增弹窗 -->
    <el-dialog title="新增物种" v-model="dialogVisible" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="中文俗名" prop="commonName">
          <el-input v-model="form.commonName" placeholder="例如：长颈鹿" />
        </el-form-item>
        <el-form-item label="学名" prop="scientificName">
          <el-input v-model="form.scientificName" placeholder="例如：Giraffa camelopardalis" />
        </el-form-item>
        <el-form-item label="保护等级" prop="conservationStatus">
          <el-select v-model="form.conservationStatus" placeholder="请选择">
            <el-option label="无危 (LC)" value="LC" />
            <el-option label="易危 (VU)" value="VU" />
            <el-option label="濒危 (EN)" value="EN" />
            <el-option label="极危 (CR)" value="CR" />
          </el-select>
        </el-form-item>
        <el-form-item label="食性" prop="dietType">
          <el-select v-model="form.dietType" placeholder="请选择">
            <el-option label="草食" value="草食" />
            <el-option label="肉食" value="肉食" />
            <el-option label="杂食" value="杂食" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAllSpecies, createSpecies } from '@/api/species'
import { ElMessage } from 'element-plus'

const list = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const form = reactive({
  commonName: '',
  scientificName: '',
  conservationStatus: '',
  dietType: ''
})

const rules = {
  commonName: [{ required: true, message: '必填', trigger: 'blur' }],
  scientificName: [{ required: true, message: '必填', trigger: 'blur' }]
}

onMounted(() => {
  getList()
})

const getList = () => {
  getAllSpecies().then(res => {
    list.value = res
  })
}

const handleAdd = () => {
  form.commonName = ''
  form.scientificName = ''
  form.conservationStatus = ''
  form.dietType = ''
  dialogVisible.value = true
}

const submitForm = () => {
  formRef.value.validate(valid => {
    if (valid) {
      createSpecies(form).then(() => {
        ElMessage.success('添加成功')
        dialogVisible.value = false
        getList()
      })
    }
  })
}
</script>

<style scoped>
.app-container { padding: 20px; }
.filter-container { margin-bottom: 20px; }
</style>