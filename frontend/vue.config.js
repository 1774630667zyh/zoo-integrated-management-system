import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue()],
    resolve: {
        alias: {
            '@': path.resolve(__dirname, 'src')
        }
    },
    server: {
        port: 3000, // 前端运行端口
        proxy: {
            // 代理配置: 遇到 /api 开头的请求，转发到后端 8080
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                // rewrite: (path) => path.replace(/^\/api/, '') // 如果后端路径不包含/api，需开启此行
            }
        }
    }
})