<template>
  <div>
    <!-- 欢迎 Banner -->
    <el-card class="banner-card" shadow="hover">
      <div class="banner-content">
        <h1>欢迎来到 ZIMS 野生动物园</h1>
        <p>探索自然奥秘，分享游园乐趣</p>
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

    <!-- 动物社区互动区 -->
    <h2 class="section-title" style="margin-top: 40px;">动物明星 & 游客社区</h2>
    <p style="color: #909399; margin-bottom: 20px;">点击卡片查看详情并参与讨论</p>
    <el-row :gutter="20">
      <el-col :span="6" v-for="animal in animals" :key="animal.id">
        <el-card shadow="hover" class="animal-card" @click="openDetail(animal)">
          <div style="text-align: center;">
            <div class="animal-avatar">{{ animal.name ? animal.name.substring(0,1) : 'Z' }}</div>
            <h3>{{ animal.name }}</h3>
            <el-tag type="info" size="small">{{ animal.gender === 1 ? '雄性' : '雌性' }}</el-tag>
            <div class="click-hint">点击查看评论</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 动物详情与评论弹窗 -->
    <el-dialog v-model="detailVisible" :title="(currentAnimal.name || '') + ' - 游客讨论区'" width="600px">
      <div class="comment-section">
        <!-- 评论列表 -->
        <div class="comment-list" v-if="comments.length > 0">
          <div v-for="c in comments" :key="c.id" class="comment-item">
            <div class="comment-header">
              <span class="nickname">{{ c.nickname }}</span>
              <el-rate v-model="c.rating" disabled size="small" />
              <span class="time">{{ c.createTime }}</span>
            </div>
            <div class="comment-content">{{ c.content }}</div>
          </div>
        </div>
        <el-empty v-else description="暂无评论，快来抢沙发！" image-size="60" />

        <el-divider />

        <!-- 发表评论 -->
        <div class="post-comment">
          <h4>发表评论</h4>
          <div v-if="visitorStore.isLoggedIn">
            <el-rate v-model="newComment.rating" style="margin-bottom: 10px;" />
            <el-input v-model="newComment.content" type="textarea" :rows="2" placeholder="分享你的看法..." />
            <el-button type="primary" size="small" style="margin-top: 10px;" @click="submitComment">发布</el-button>
          </div>
          <div v-else class="login-tip">
            <el-button link type="primary" @click="$router.push('/visitor/auth')">登录</el-button> 后参与讨论
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { getVisitorProducts, createVisitorOrder, getVisitorAnimals, getComments, postComment } from '@/api/visitor'
import { useVisitorStore } from '@/store/visitorUser'
import { ElMessage, ElMessageBox } from 'element-plus'

const visitorStore = useVisitorStore()
const products = ref([])
const animals = ref([])
const selectedProduct = ref({})
const loading = ref(false)

// 订单表单
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

// 详情弹窗相关
const detailVisible = ref(false)
const currentAnimal = ref({})
const comments = ref([])
const newComment = reactive({ rating: 5, content: '' })

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
    if (products.value.length > 0) {
      selectedProduct.value = products.value[0]
    }
  })
}

const loadAnimals = () => {
  getVisitorAnimals().then(res => {
    animals.value = res
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
        payStatus: 1,
        ticketJson: [{
          type: selectedProduct.value.name,
          price: selectedProduct.value.price,
          count: form.count
        }]
      }

      createVisitorOrder(orderData).then(res => {
        loading.value = false
        ElMessageBox.alert(`下单成功！您的取票号是：${res}`, '支付成功', {
          confirmButtonText: '确定'
        })
      }).catch(() => {
        loading.value = false
      })
    }
  })
}

// --- 社区逻辑 ---

const openDetail = (animal) => {
  currentAnimal.value = animal
  detailVisible.value = true
  loadComments(animal.id)
}

const loadComments = (animalId) => {
  getComments('animal', animalId).then(res => {
    comments.value = res || []
  })
}

const submitComment = () => {
  if (!newComment.content) return ElMessage.warning('请输入评论内容')

  postComment({
    type: 'animal',
    targetId: currentAnimal.value.id,
    content: newComment.content,
    rating: newComment.rating,
    nickname: visitorStore.nickname
  }).then(() => {
    ElMessage.success('评论发布成功')
    newComment.content = ''
    loadComments(currentAnimal.value.id)
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

/* Community Styles */
.animal-card {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
}
.animal-card:hover {
  transform: scale(1.02);
}
.animal-avatar {
  width: 60px;
  height: 60px;
  background: #e1f3d8;
  border-radius: 50%;
  line-height: 60px;
  font-size: 24px;
  color: #67c23a;
  margin: 0 auto 10px;
  font-weight: bold;
}
.click-hint {
  font-size: 12px;
  color: #409EFF;
  margin-top: 10px;
  opacity: 0;
  transition: opacity 0.3s;
}
.animal-card:hover .click-hint {
  opacity: 1;
}

.comment-list {
  max-height: 300px;
  overflow-y: auto;
  margin-bottom: 20px;
}
.comment-item {
  border-bottom: 1px solid #eee;
  padding: 10px 0;
}
.comment-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 5px;
}
.nickname {
  font-weight: bold;
  font-size: 14px;
  color: #333;
}
.time {
  font-size: 12px;
  color: #999;
  margin-left: auto;
}
.comment-content {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}
.login-tip {
  text-align: center;
  color: #999;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 4px;
}
</style>