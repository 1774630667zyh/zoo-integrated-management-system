import { defineStore } from 'pinia'
import { login, logout } from '@/api/user.js'

export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('ZIMS-Token') || '',
        name: '',
        role: ''
    }),
    actions: {
        // 登录动作
        login(userInfo) {
            const { username, password } = userInfo
            return new Promise((resolve, reject) => {
                login({ username: username.trim(), password: password })
                    .then(data => {
                        // 后端返回 { token: '...', userInfo: {...} }
                        const { token, userInfo } = data
                        this.token = token
                        this.name = userInfo.realName
                        this.role = userInfo.role

                        // 持久化 Token
                        localStorage.setItem('ZIMS-Token', token)
                        resolve()
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },

        // 登出
        logout() {
            return new Promise((resolve) => {
                // 前端登出：清除 Token 和状态
                this.token = ''
                this.name = ''
                this.role = ''
                localStorage.removeItem('ZIMS-Token')
                resolve()
            })
        }
    }
})