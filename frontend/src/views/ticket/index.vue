<template>
  <div class="app-container">
    <el-card class="filter-container">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="订单号">
          <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-container">
      <el-table v-loading="loading" :data="ticketList" border>
        <el-table-column prop="orderNo" label="订单号" width="220" />
        <el-table-column prop="mobile" label="联系手机" width="150" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }">
            ¥ {{ row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="visitDate" label="入园日期" width="120" />
        <el-table-column prop="payStatus" label="支付状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.payStatus === 1" type="success">已支付</el-tag>
            <el-tag v-else-if="row.payStatus === 0" type="warning">未支付</el-tag>
            <el-tag v-else type="info">已退款</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="180" />
        <!-- 这里简单展示 JSON 详情 -->
        <el-table-column prop="ticketJson" label="购票详情" min-width="200" show-overflow-tooltip />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTicketList } from '@/api/ticket'

const loading = ref(false)
const total = ref(0)
const ticketList = ref([])

const queryParams = reactive({
  page: 1,
  size: 10,
  orderNo: ''
})

onMounted(() => {
  getList()
})

const getList = () => {
  loading.value = true
  getTicketList(queryParams).then(res => {
    ticketList.value = res.records
    total.value = res.total
    loading.value = false
  })
}

const handleQuery = () => {
  queryParams.page = 1
  getList()
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.filter-container {
  margin-bottom: 20px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>