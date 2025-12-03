<template>
  <div class="visitor-layout">
    <el-container>
      <el-header class="visitor-header">
        <div class="logo" @click="$router.push('/visitor/home')">
          <el-icon><StarFilled /></el-icon> ZIMS 动物园 - 游客服务中心
        </div>
        <div class="right-menu">
          <el-menu mode="horizontal" router :default-active="$route.path" class="nav-menu">
            <el-menu-item index="/visitor/home">首页 & 购票</el-menu-item>
            <el-menu-item index="/visitor/my-orders">我的订单</el-menu-item>
          </el-menu>

          <div class="user-area">
            <template v-if="store.isLoggedIn">
              <span class="welcome">你好, {{ store.nickname }}</span>
              <el-button link type="danger" @click="handleLogout">退出</el-button>
            </template>
            <template v-else>
              <el-button type="primary" size="small" @click="$router.push('/visitor/auth')">登录 / 注册</el-button>
            </template>
          </div>
        </div>
      </el-header>
      <el-main class="visitor-main">
        <router-view />
      </el-main>
      <el-footer class="visitor-footer">
        © 2023 Zoo Management System | 开放时间: 09:00 - 18:00
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { useVisitorStore } from '@/store/visitorUser'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const store = useVisitorStore()
const router = useRouter()

const handleLogout = () => {
  store.logout()
  ElMessage.success('已退出登录')
  router.push('/visitor/home')
}
</script>

<style scoped>
.visitor-layout {
  min-height: 100vh;
  background-color: #f5f7fa;
}
.visitor-header {
  background: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  padding: 0 50px;
}
.logo {
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
}
.right-menu {
  display: flex;
  align-items: center;
  gap: 20px;
}
.nav-menu {
  border-bottom: none;
  flex: 1;
}
.user-area {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
}
.welcome {
  color: #606266;
}
.visitor-main {
  width: 100%;
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
}
.visitor-footer {
  text-align: center;
  color: #909399;
  padding: 20px;
  background: #fff;
  margin-top: auto;
}
</style>