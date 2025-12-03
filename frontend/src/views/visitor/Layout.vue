<template>
  <div class="visitor-layout">
    <el-container>
      <el-header class="visitor-header">
        <div class="logo">
          <el-icon><StarFilled /></el-icon> ZIMS 动物园 - 游客服务中心
        </div>
        <el-menu mode="horizontal" router :default-active="$route.path" class="nav-menu">
          <el-menu-item index="/visitor/home">在线购票</el-menu-item>
          <el-menu-item index="/visitor/my-orders">我的订单</el-menu-item>
          <el-menu-item index="/login">员工入口</el-menu-item>
        </el-menu>
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
  display: flex;
  align-items: center;
  gap: 10px;
}
.nav-menu {
  border-bottom: none;
  flex: 1;
  justify-content: flex-end;
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