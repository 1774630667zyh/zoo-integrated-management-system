<template>
  <div class="app-container">
    <el-card class="filter-container">
      <el-form :inline="true">
        <el-form-item label="筛选场馆">
          <el-select v-model="queryParams.facilityId" placeholder="全部" clearable @change="getList">
            <el-option v-for="item in facilityOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="warning" icon="Warning" @click="handleReport">提交报修</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-container">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column prop="id" label="工单ID" width="80" align="center" />
        <el-table-column prop="facilityId" label="报修场馆" width="150">
          <template #default="{ row }">{{ getFacilityName(row.facilityId) }}</template>
        </el-table-column>
        <el-table-column prop="description" label="故障描述" min-width="200" />
        <el-table-column prop="createTime" label="报修时间" width="180" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="danger">待处理</el-tag>
            <el-tag v-else-if="row.status === 1" type="warning">维修中</el-tag>
            <el-tag v-else type="success">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="result" label="维修结果" min-width="150" />
        <el-table-column label="操作" align="center" width="120">
          <template #default="{ row }">
            <el-button
                v-if="row.status !== 2"
                type="success"
                size="small"
                @click="handleComplete(row)"
            >
              完成维修
            </el-button>
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

    <!-- 报修弹窗 -->
    <el-dialog title="提交故障报修" v-model="reportDialog.visible" width="500px">
      <el-form ref="reportFormRef" :model="reportForm" :rules="reportRules" label-width="80px">
        <el-form-item label="场馆" prop="facilityId">
          <el-select v-model="reportForm.facilityId" placeholder="请选择故障场馆" style="width: 100%">
            <el-option v-for="item in facilityOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
              v-model="reportForm.description"
              type="textarea"
              :rows="3"
              placeholder="请详细描述故障情况，例如：大门门锁损坏"
          />
        </el-form-item>
      </el-form>
      <div class="dialog-tips">
        <el-icon><InfoFilled /></el-icon>
        注意：提交报修后，该场馆状态将自动变更为“维护中”。
      </div>
      <template #footer>
        <el-button @click="reportDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submitReport">提交</el-button>
      </template>
    </el-dialog>

    <!-- 完成维修弹窗 -->
    <el-dialog title="完成维修" v-model="completeDialog.visible" width="500px">
      <el-form>
        <el-form-item label="维修结果">
          <el-input
              v-model="completeResult"
              type="textarea"
              :rows="3"
              placeholder="请填写维修处理结果"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeDialog.visible = false">取消</el-button>
        <el-button type="success" @click="submitComplete">确认完成</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMaintenanceList, reportMaintenance, completeMaintenance } from '@/api/maintenance'
import { getAllFacilities } from '@/api/facility'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const facilityOptions = ref([])
const facilityMap = ref({})

const queryParams = reactive({
  page: 1,
  size: 10,
  facilityId: undefined
})

// 报修相关
const reportDialog = reactive({ visible: false })
const reportFormRef = ref(null)
const reportForm = reactive({ facilityId: undefined, description: '' })
const reportRules = {
  facilityId: [{ required: true, message: '请选择场馆', trigger: 'change' }],
  description: [{ required: true, message: '请填写描述', trigger: 'blur' }]
}

// 完成相关
const completeDialog = reactive({ visible: false, id: null })
const completeResult = ref('')

onMounted(() => {
  loadFacilities()
  getList()
})

const loadFacilities = async () => {
  const res = await getAllFacilities()
  if (res) {
    facilityOptions.value = res
    res.forEach(item => facilityMap.value[item.id] = item.name)
  }
}

const getFacilityName = (id) => facilityMap.value[id] || id

const getList = () => {
  loading.value = true
  getMaintenanceList(queryParams).then(res => {
    list.value = res.records
    total.value = res.total
    loading.value = false
  })
}

// --- 报修逻辑 ---
const handleReport = () => {
  reportForm.facilityId = undefined
  reportForm.description = ''
  reportDialog.visible = true
}

const submitReport = () => {
  reportFormRef.value.validate(valid => {
    if (valid) {
      reportMaintenance({ ...reportForm, reporterId: 1 }).then(() => {
        ElMessage.success('报修成功')
        reportDialog.visible = false
        getList()
      })
    }
  })
}

// --- 完成逻辑 ---
const handleComplete = (row) => {
  completeDialog.id = row.id
  completeResult.value = ''
  completeDialog.visible = true
}

const submitComplete = () => {
  if (!completeResult.value) return ElMessage.warning('请填写结果')

  completeMaintenance(completeDialog.id, completeResult.value).then(() => {
    ElMessage.success('工单已完成')
    completeDialog.visible = false
    getList()
  })
}
</script>

<style scoped>
.app-container { padding: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
.dialog-tips {
  margin-top: 10px;
  color: #e6a23c;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>