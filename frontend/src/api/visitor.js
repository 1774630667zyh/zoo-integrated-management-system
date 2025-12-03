import request from '@/utils/request'

// --- 基础业务 ---
export function getVisitorProducts() {
    return request({ url: '/visitor/products', method: 'get' })
}

export function createVisitorOrder(data) {
    return request({ url: '/visitor/order', method: 'post', data })
}

export function getVisitorOrders(mobile) {
    return request({ url: '/visitor/orders', method: 'get', params: { mobile } })
}

export function getVisitorAnimals() {
    return request({ url: '/visitor/animals', method: 'get' })
}

// --- 账号相关 ---
export function visitorLogin(data) {
    return request({ url: '/visitor/login', method: 'post', data })
}

export function visitorRegister(data) {
    return request({ url: '/visitor/register', method: 'post', data })
}

// --- 社区相关 ---
export function getComments(type, targetId) {
    return request({ url: `/visitor/comments/list/${type}/${targetId}`, method: 'get' })
}

export function postComment(data) {
    return request({ url: '/visitor/comment', method: 'post', data })
}