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
    // 票务管理
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
    // 系统管理 (新增)
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
        redirect: '/dashboard',
        hidden: true
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('ZIMS-Token')
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