import { createRouter, createWebHistory } from 'vue-router'

// 静态路由表
const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/index.vue'),
        hidden: true
    },
    // --- 游客端路由 (更新) ---
    {
        path: '/visitor',
        component: () => import('@/views/visitor/Layout.vue'),
        redirect: '/visitor/home',
        hidden: true, // 隐藏此路由，防止管理员侧边栏渲染时报错
        children: [
            {
                path: 'home',
                name: 'VisitorHome',
                component: () => import('@/views/visitor/Home.vue'),
                meta: { title: '在线购票' }
            },
            {
                path: 'my-orders',
                name: 'VisitorOrders',
                component: () => import('@/views/visitor/MyOrders.vue'),
                meta: { title: '我的订单' }
            },
            // [新增] 游客登录页
            {
                path: 'auth',
                name: 'VisitorAuth',
                component: () => import('@/views/visitor/Login.vue'),
                meta: { title: '游客登录' }
            }
        ]
    },
    // --- 管理端路由 ---
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
    // 动物管理
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
            {
                path: 'species',
                name: 'SpeciesList',
                component: () => import('@/views/species/index.vue'),
                meta: { title: '物种字典' }
            }
        ]
    },
    // 饲养与营养
    {
        path: '/food',
        component: () => import('@/layout/index.vue'),
        children: [
            {
                path: 'index',
                name: 'FoodInventory',
                component: () => import('@/views/food/index.vue'),
                meta: { title: '饲料库存', icon: 'Dish' }
            }
        ]
    },
    // 设施维护
    {
        path: '/facility',
        component: () => import('@/layout/index.vue'),
        redirect: '/facility/list',
        meta: { title: '设施维护', icon: 'OfficeBuilding' },
        children: [
            {
                path: 'list',
                name: 'FacilityList',
                component: () => import('@/views/facility/index.vue'),
                meta: { title: '场馆管理' }
            },
            {
                path: 'maintenance',
                name: 'MaintenanceList',
                component: () => import('@/views/maintenance/index.vue'),
                meta: { title: '维修工单' }
            }
        ]
    },
    // 票务管理 (后台)
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
    // 系统管理
    {
        path: '/system',
        component: () => import('@/layout/index.vue'),
        meta: { title: '系统管理', icon: 'Setting' },
        children: [
            {
                path: 'staff',
                name: 'StaffList',
                component: () => import('@/views/staff/index.vue'),
                meta: { title: '员工管理' }
            }
        ]
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: '/login', // 默认跳登录
        hidden: true
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('ZIMS-Token')

    // 游客端页面直接放行
    if (to.path.startsWith('/visitor')) {
        next()
        return
    }

    const whiteList = ['/login']
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