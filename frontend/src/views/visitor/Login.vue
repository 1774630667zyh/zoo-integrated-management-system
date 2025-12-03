<template>
  <div class="auth-container">
    <el-card class="auth-card">
      <template #header>
        <div class="tabs">
          <span :class="{active: isLogin}" @click="isLogin = true">登录</span>
          <span :class="{active: !isLogin}" @click="isLogin = false">注册</span>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="账号/手机号" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item v-if="!isLogin" prop="nickname">
          <el-input v-model="form.nickname" placeholder="您的昵称" prefix-icon="Postcard" />
        </el-form-item>

        <el-button type="primary" class="submit-btn" :loading="loading" @click="handleSubmit">
          {{ isLogin ? '立即登录' : '注册账号' }}
        </el-button>

        <div class="skip-login">
          <el-button link @click="$router.push('/visitor/home')">暂不登录，直接逛逛</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useVisitorStore } from '@/store/visitorUser'
import { visitorRegister } from '@/api/visitor'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const store = useVisitorStore()
const isLogin = ref(true)
const loading = ref(false)
const formRef = ref(null)

const form = reactive({ username: '', password: '', nickname: '' })

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleSubmit = () => {
  formRef.value.validate(valid => {
    if (!valid) return
    loading.value = true

    if (isLogin.value) {
      store.login(form).then(() => {
        ElMessage.success('欢迎回来 ' + store.nickname)
        router.push('/visitor/home')
      }).catch(() => { loading.value = false })
    } else {
      visitorRegister(form).then(() => {
        ElMessage.success('注册成功，请登录')
        isLogin.value = true
        loading.value = false
      }).catch(() => { loading.value = false })
    }
  })
}
</script>

<style scoped>
.auth-container {
  display: flex; justify-content: center; align-items: center;
  height: 80vh; background: #f0f2f5;
}
.auth-card { width: 360px; }
.tabs { display: flex; justify-content: space-around; cursor: pointer; font-size: 18px; margin-bottom: 10px; }
.tabs span { padding-bottom: 5px; }
.tabs span.active { color: #409EFF; border-bottom: 2px solid #409EFF; font-weight: bold; }
.submit-btn { width: 100%; margin-top: 10px; }
.skip-login { text-align: center; margin-top: 15px; }
</style>