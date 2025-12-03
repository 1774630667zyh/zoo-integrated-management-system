import request from '@/utils/request'

// 分页获取场馆列表
export function getFacilityList(params) {
    return request({
        url: '/facilities',
        method: 'get',
        params
    })
}

// 获取所有场馆 (用于下拉框)
export function getAllFacilities() {
    return request({
        url: '/facilities/list',
        method: 'get'
    })
}

// 新增场馆
export function createFacility(data) {
    return request({
        url: '/facilities',
        method: 'post',
        data
    })
}

// 修改场馆信息
export function updateFacility(id, data) {
    return request({
        url: `/facilities/${id}`,
        method: 'put',
        data
    })
}

// 删除场馆
export function deleteFacility(id) {
    return request({
        url: `/facilities/${id}`,
        method: 'delete'
    })
}