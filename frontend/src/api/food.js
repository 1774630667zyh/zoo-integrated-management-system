import request from '@/utils/request'

// 分页获取饲料列表
export function getFoodList(params) {
    return request({
        url: '/foods',
        method: 'get',
        params
    })
}

// 获取所有饲料 (用于下拉框选择)
export function getAllFoods() {
    return request({
        url: '/foods/list',
        method: 'get'
    })
}

// 新增饲料
export function createFood(data) {
    return request({
        url: '/foods',
        method: 'post',
        data
    })
}

// 修改饲料
export function updateFood(id, data) {
    return request({
        url: `/foods/${id}`,
        method: 'put',
        data
    })
}

// 删除饲料
export function deleteFood(id) {
    return request({
        url: `/foods/${id}`,
        method: 'delete'
    })
}