<template>
  <!-- 使用 ConfigProvider 全局配置中文 -->
  <el-config-provider :locale="zhCn">
    <router-view />
  </el-config-provider>
</template>

<script setup>
import { ElConfigProvider } from 'element-plus'
// 引入中文语言包
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

// App.vue 是应用的根组件
// router-view 是路由出口，所有页面都会渲染在这里
</script>

<style>
/* 全局重置样式，确保页面占满全屏 */
html, body, #app {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}
</style>