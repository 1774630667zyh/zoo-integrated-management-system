import { defineStore } from 'pinia'
import { visitorLogin } from '@/api/visitor.js'

export const useVisitorStore = defineStore('visitorUser', {
    state: () => ({
        token: localStorage.getItem('ZIMS-Visitor-Token') || '',
        info: JSON.parse(localStorage.getItem('ZIMS-Visitor-Info') || '{}')
    }),
    getters: {
        isLoggedIn: (state) => !!state.token,
        nickname: (state) => state.info.nickname || '游客'
    },
    actions: {
        login(loginForm) {
            return new Promise((resolve, reject) => {
                visitorLogin(loginForm).then(data => {
                    const { token, userInfo } = data
                    this.token = token
                    this.info = userInfo

                    // 存储游客专属Token
                    localStorage.setItem('ZIMS-Visitor-Token', token)
                    localStorage.setItem('ZIMS-Visitor-Info', JSON.stringify(userInfo))

                    // 为了复用 request.js 中的拦截器逻辑（它读取 ZIMS-Token），
                    // 我们临时覆盖主 Token。
                    localStorage.setItem('ZIMS-Token', token)

                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },
        logout() {
            this.token = ''
            this.info = {}
            localStorage.removeItem('ZIMS-Visitor-Token')
            localStorage.removeItem('ZIMS-Visitor-Info')
            // 清除可能被覆盖的主Token
            localStorage.removeItem('ZIMS-Token')
        }
    }
})