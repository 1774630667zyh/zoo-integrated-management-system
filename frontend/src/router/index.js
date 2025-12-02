import { createRouter, createWebHistory } from 'vue-router'

// 静态路由表
const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/index.vue'),
        hidden: true
    },
    {
        path: '/',
        component: () => import('@/layout/index.vue'),
        redirect: '/dashboard',
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('@/views/dashboard/index.vue'),
                meta: { title: '首页', icon: 'House' }
            }
        ]
    },
    {
        path: '/animal',
        component: () => import('@/layout/index.vue'),
        redirect: '/animal/list',
        meta: { title: '动物管理', icon: 'Collection' },
        children: [
            {
                path: 'list',
                name: 'AnimalList',
                component: () => import('@/views/animal/index.vue'),
                meta: { title: '动物档案' }
            },
            // 这里预留后续的医疗和物种页面
        ]
    },
    {
        path: '/ticket',
        component: () => import('@/layout/index.vue'),
        children: [
            {
                path: 'order',
                name: 'TicketOrder',
                component: () => import('@/views/ticket/index.vue'),
                meta: { title: '票务订单', icon: 'Ticket' }
            }
        ]
    },
    // 404 页面
    {
        path: '/:pathMatch(.*)*',
        redirect: '/dashboard',
        hidden: true
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('ZIMS-Token')
    const whiteList = ['/login'] // 白名单

    if (token) {
        if (to.path === '/login') {
            next({ path: '/' })
        } else {
            next()
        }
    } else {
        if (whiteList.indexOf(to.path) !== -1) {
            next()
        } else {
            next('/login')
        }
    }
})

export default router