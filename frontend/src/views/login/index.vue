<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <h2>Zoo Management System</h2>
          <p>动物园综合管理平台</p>
        </div>
      </template>

      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
              v-model="loginForm.username"
              placeholder="请输入工号/用户名"
              prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
          />
        </el-form-item>

        <!-- 员工登录按钮 -->
        <el-form-item>
          <el-button
              type="primary"
              :loading="loading"
              class="login-button"
              @click="handleLogin"
          >
            {{ loading ? '员工登录...' : '员 工 登 录' }}
          </el-button>
        </el-form-item>

        <!-- [新增] 游客入口按钮 (绿色醒目样式) -->
        <el-form-item>
          <el-button type="success" plain class="login-button" @click="goToVisitor">
            <el-icon style="margin-right: 5px"><Bicycle /></el-icon> 游客购票通道 (无需登录)
          </el-button>
        </el-form-item>

        <div class="tips">
          <span>默认管理员: admin / 123456</span>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
  password: [{ required: true, trigger: 'blur', message: '请输入密码' }]
}

const loading = ref(false)

const handleLogin = () => {
  loginFormRef.value.validate(valid => {
    if (valid) {
      loading.value = true
      userStore.login(loginForm)
          .then(() => {
            ElMessage.success('登录成功')
            router.push({ path: '/' })
            loading.value = false
          })
          .catch(() => {
            loading.value = false
          })
    } else {
      return false
    }
  })
}

// 跳转到游客端
const goToVisitor = () => {
  router.push('/visitor/home')
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #2d3a4b;
  background-image: linear-gradient(135deg, #2d3a4b 0%, #1f2a38 100%);
}

.login-card {
  width: 400px;
  border-radius: 8px;
}

.login-header {
  text-align: center;
}

.login-header h2 {
  margin: 0;
  color: #303133;
}

.login-header p {
  margin: 10px 0 0;
  color: #909399;
  font-size: 14px;
}

.login-form {
  padding: 20px 0;
}

.login-button {
  width: 100%;
}

.tips {
  font-size: 12px;
  color: #909399;
  text-align: center;
  margin-top: 10px;
}
</style>