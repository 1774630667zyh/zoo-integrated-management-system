<template>
  <div>
    <!-- 欢迎 Banner -->
    <el-card class="banner-card" shadow="hover">
      <div class="banner-content">
        <h1>欢迎来到 ZIMS 野生动物园</h1>
        <p>探索自然奥秘，与动物朋友亲密接触</p>
      </div>
    </el-card>

    <!-- 票种选择 -->
    <h2 class="section-title">门票预订</h2>
    <el-row :gutter="20">
      <el-col :span="6" v-for="ticket in products" :key="ticket.id">
        <el-card class="ticket-card" :class="{'active': selectedProduct.id === ticket.id}" @click="selectTicket(ticket)">
          <h3>{{ ticket.name }}</h3>
          <div class="price">¥ {{ ticket.price }}</div>
          <p class="desc">{{ ticket.description }}</p>
          <div class="check-icon" v-if="selectedProduct.id === ticket.id">
            <el-icon><Check /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 购票表单 -->
    <el-card class="order-form-card" v-if="selectedProduct.id">
      <template #header>
        <div class="card-header">
          <span>填写订单信息</span>
          <el-tag>{{ selectedProduct.name }}</el-tag>
        </div>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="入园日期" prop="visitDate">
          <el-date-picker
              v-model="form.visitDate"
              type="date"
              placeholder="选择游玩日期"
              :disabled-date="disabledDate"
              value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="购买数量" prop="count">
          <el-input-number v-model="form.count" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="手机号码" prop="mobile">
          <el-input v-model="form.mobile" placeholder="用于接收取票码" />
        </el-form-item>
        <el-divider />
        <div class="total-bar">
          <span>总金额：<span class="total-price">¥ {{ totalAmount }}</span></span>
          <el-button type="primary" size="large" @click="submitOrder" :loading="loading">立即支付</el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 园内精彩 -->
    <h2 class="section-title" style="margin-top: 40px;">园内精彩</h2>
    <el-row :gutter="20">
      <el-col :span="6" v-for="animal in animals" :key="animal.id">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <h3>{{ animal.name }}</h3>
            <el-tag type="info">{{ animal.gender === 1 ? '雄性' : '雌性' }}</el-tag>
            <p style="color: #666; font-size: 13px;">RFID: {{ animal.chipCode }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { getVisitorProducts, createVisitorOrder, getVisitorAnimals } from '@/api/visitor'
import { ElMessage, ElMessageBox } from 'element-plus'

const products = ref([])
const animals = ref([])
const selectedProduct = ref({})
const loading = ref(false)

const form = reactive({
  visitDate: '',
  count: 1,
  mobile: ''
})

const rules = {
  visitDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { min: 11, max: 11, message: '请输入11位手机号', trigger: 'blur' }
  ]
}

const formRef = ref(null)

// 禁止选择今天之前的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

const totalAmount = computed(() => {
  if (!selectedProduct.value.price) return 0
  return selectedProduct.value.price * form.count
})

onMounted(() => {
  loadProducts()
  loadAnimals()
})

const loadProducts = () => {
  getVisitorProducts().then(res => {
    products.value = res
    // 默认选中第一个
    if (products.value.length > 0) {
      selectedProduct.value = products.value[0]
    }
  })
}

const loadAnimals = () => {
  getVisitorAnimals().then(res => {
    // 只取前4个展示
    animals.value = res.slice(0, 4)
  })
}

const selectTicket = (ticket) => {
  selectedProduct.value = ticket
}

const submitOrder = () => {
  formRef.value.validate(valid => {
    if (valid) {
      loading.value = true

      const orderData = {
        mobile: form.mobile,
        visitDate: form.visitDate,
        totalAmount: totalAmount.value,
        payStatus: 1, // 模拟直接支付成功
        // 构造 ticketJson
        ticketJson: [{
          type: selectedProduct.value.name,
          price: selectedProduct.value.price,
          count: form.count
        }]
      }

      createVisitorOrder(orderData).then(res => {
        loading.value = false
        ElMessageBox.alert(`下单成功！您的取票号是：${res}`, '支付成功', {
          confirmButtonText: '确定',
          callback: () => {
            // 重置
            form.mobile = ''
          }
        })
      }).catch(() => {
        loading.value = false
      })
    }
  })
}
</script>

<style scoped>
.banner-card {
  background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
  color: #fff;
  text-align: center;
  border: none;
  margin-bottom: 30px;
}
.banner-content h1 { margin: 0; font-size: 2.5rem; }
.section-title {
  margin-bottom: 20px;
  border-left: 5px solid #409EFF;
  padding-left: 10px;
  color: #303133;
}
.ticket-card {
  cursor: pointer;
  position: relative;
  transition: all 0.3s;
  text-align: center;
}
.ticket-card:hover { transform: translateY(-5px); }
.ticket-card.active { border: 2px solid #409EFF; background-color: #ecf5ff; }
.price { color: #f56c6c; font-size: 24px; font-weight: bold; margin: 10px 0; }
.desc { color: #909399; font-size: 13px; height: 40px; }
.check-icon { position: absolute; top: 5px; right: 5px; color: #409EFF; }
.order-form-card { margin-top: 30px; }
.total-bar { display: flex; justify-content: space-between; align-items: center; }
.total-price { color: #f56c6c; font-size: 28px; font-weight: bold; margin-right: 20px; }
</style>