import request from '@/utils/request'

// 获取所有在售票种
export function getVisitorProducts() {
    return request({
        url: '/visitor/products',
        method: 'get'
    })
}

// 游客下单
export function createVisitorOrder(data) {
    return request({
        url: '/visitor/order',
        method: 'post',
        data
    })
}

// 游客查询订单
export function getVisitorOrders(mobile) {
    return request({
        url: '/visitor/orders',
        method: 'get',
        params: { mobile }
    })
}

// 获取展示的动物
export function getVisitorAnimals() {
    return request({
        url: '/visitor/animals',
        method: 'get'
    })
}

// 获取展示的场馆
export function getVisitorFacilities() {
    return request({
        url: '/visitor/facilities',
        method: 'get'
    })
}