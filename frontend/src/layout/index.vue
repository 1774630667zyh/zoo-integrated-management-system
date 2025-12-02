<template>
  <el-container class="app-wrapper">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="sidebar-container">
      <div class="logo">
        <el-icon class="logo-icon"><Stopwatch /></el-icon>
        <span v-show="!isCollapse">ZIMS 管理系统</span>
      </div>
      <el-scrollbar>
        <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical"
            background-color="#304156"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
            :collapse="isCollapse"
            router
        >
          <!-- 动态生成菜单：这里简单处理，遍历路由表 -->
          <!-- 实际项目中通常封装为 SidebarItem 组件递归处理 -->
          <template v-for="route in menuRoutes" :key="route.path">
            <!-- 单级菜单 -->
            <el-menu-item
                v-if="!route.children || route.children.length === 1"
                :index="resolvePath(route)"
            >
              <el-icon v-if="getMeta(route).icon">
                <component :is="getMeta(route).icon" />
              </el-icon>
              <span>{{ getMeta(route).title }}</span>
            </el-menu-item>

            <!-- 多级菜单 (这里只处理了两级，足以应对当前需求) -->
            <el-sub-menu v-else :index="route.path">
              <template #title>
                <el-icon v-if="route.meta && route.meta.icon">
                  <component :is="route.meta.icon" />
                </el-icon>
                <span>{{ route.meta.title }}</span>
              </template>
              <el-menu-item
                  v-for="child in route.children"
                  :key="child.path"
                  :index="resolvePath(route, child)"
              >
                {{ child.meta.title }}
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 主体区域 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header class="navbar">
        <div class="left-panel">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="right-panel">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              管理员
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isCollapse = ref(false)

// 过滤掉隐藏的路由 (如 login, 404)
const menuRoutes = computed(() => {
  return router.options.routes.filter(r => !r.hidden)
})

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title)

// 辅助函数：获取路由元数据
const getMeta = (route) => {
  if (route.children && route.children.length === 1) {
    return route.children[0].meta
  }
  return route.meta
}

// 辅助函数：解析路径
const resolvePath = (parent, child) => {
  if (!child) {
    // 如果是单级路由且有children (如 dashboard), 取子路径
    if (parent.children && parent.children.length === 1) {
      return parent.path === '/' ? '/' + parent.children[0].path : parent.path + '/' + parent.children[0].path
    }
    return parent.path
  }
  return parent.path + '/' + child.path
}

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout().then(() => {
      router.push('/login')
    })
  }
}
</script>

<style scoped>
.app-wrapper {
  height: 100vh;
  width: 100%;
}

.sidebar-container {
  background-color: #304156;
  color: #fff;
  height: 100%;
  overflow: hidden;
}

.logo {
  height: 50px;
  line-height: 50px;
  text-align: center;
  font-weight: 600;
  font-size: 20px;
  color: #fff;
  background-color: #2b2f3a;
  display: flex;
  align-items: center;
  justify-content: center;
}
.logo-icon {
  margin-right: 8px;
  font-size: 24px;
}

.el-menu-vertical {
  border-right: none;
}

.main-container {
  height: 100vh;
  background-color: #f0f2f5;
  display: flex;
  flex-direction: column;
}

.navbar {
  height: 50px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.app-main {
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}

/* 简单的过渡动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>