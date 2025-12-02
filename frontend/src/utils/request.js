import axios from 'axios'
import { ElMessage } from 'element-plus'
// 暂时假设 store 文件尚未创建，这里使用 localStorage 获取 token
// 后续会替换为 import { useUserStore } from '@/store/user'

const service = axios.create({
    baseURL: '/api', // 对应 vite.config.js 中的代理
    timeout: 5000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
    config => {
        // 从 localStorage 获取 Token (TokenKey = 'ZIMS-Token')
        const token = localStorage.getItem('ZIMS-Token')
        if (token) {
            // 按照后端 LoginInterceptor 的要求，添加 Bearer 前缀
            config.headers['Authorization'] = 'Bearer ' + token
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
service.interceptors.response.use(
    response => {
        const res = response.data
        // 后端 Result 结构: { flag, code, message, data }
        // 20000 是后端定义的 StatusCode.OK
        if (res.code !== 20000) {
            ElMessage({
                message: res.message || '系统错误',
                type: 'error',
                duration: 5 * 1000
            })

            // 20003: Token过期或未登录 (StatusCode.ACCESS_ERROR / LOGIN_ERROR)
            if (res.code === 20003 || res.code === 20002) {
                // 清除 Token 并跳转登录页
                localStorage.removeItem('ZIMS-Token')
                window.location.href = '/login'
            }
            return Promise.reject(new Error(res.message || 'Error'))
        } else {
            return res.data // 直接返回数据部分
        }
    },
    error => {
        console.log('err' + error)
        ElMessage({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
        })
        return Promise.reject(error)
    }
)

export default service