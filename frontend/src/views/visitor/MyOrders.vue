<template>
  <div>
    <el-card>
      <div class="search-bar">
        <el-input
            v-model="mobile"
            placeholder="请输入下单手机号查询订单"
            style="width: 300px; margin-right: 10px;"
            prefix-icon="Iphone"
        />
        <el-button type="primary" @click="handleSearch" :loading="loading">查询订单</el-button>
      </div>
    </el-card>

    <div class="order-list" v-if="orders.length > 0">
      <el-card v-for="order in orders" :key="order.orderNo" class="order-item">
        <template #header>
          <div class="order-header">
            <span>订单号: {{ order.orderNo }}</span>
            <el-tag :type="order.payStatus === 1 ? 'success' : 'warning'">
              {{ order.payStatus === 1 ? '已支付' : '未支付' }}
            </el-tag>
          </div>
        </template>
        <div class="order-content">
          <div class="info-row">
            <span class="label">入园日期:</span>
            <span class="value">{{ order.visitDate }}</span>
          </div>
          <div class="info-row">
            <span class="label">总金额:</span>
            <span class="value price">¥ {{ order.totalAmount }}</span>
          </div>
          <div class="info-row">
            <span class="label">票务详情:</span>
            <span class="value" v-if="order.ticketJson">
              <span v-for="(item, idx) in order.ticketJson" :key="idx">
                {{ item.type }} x {{ item.count }}张
              </span>
            </span>
          </div>
        </div>
      </el-card>
    </div>

    <el-empty v-else description="请输入手机号查询或暂无订单" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getVisitorOrders } from '@/api/visitor'
import { ElMessage } from 'element-plus'

const mobile = ref('')
const loading = ref(false)
const orders = ref([])

const handleSearch = () => {
  if (!mobile.value) return ElMessage.warning('请输入手机号')

  loading.value = true
  getVisitorOrders(mobile)
      .then(res => {
        orders.value = res || []
        if (orders.value.length === 0) {
          ElMessage.info('未查到该手机号的订单')
        }
      })
      .finally(() => {
        loading.value = false
      })
}
</script>

<style scoped>
.search-bar { display: flex; justify-content: center; padding: 20px 0; }
.order-list { margin-top: 20px; }
.order-item { margin-bottom: 20px; }
.order-header { display: flex; justify-content: space-between; align-items: center; }
.info-row { margin-bottom: 10px; font-size: 14px; }
.label { color: #909399; width: 80px; display: inline-block; }
.price { color: #f56c6c; font-weight: bold; }
</style>