import request from '@/utils/request'

// 获取所有物种 (用于下拉框)
export function getAllSpecies() {
    return request({
        url: '/species/list',
        method: 'get'
    })
}

// 新增物种
export function createSpecies(data) {
    return request({
        url: '/species',
        method: 'post',
        data
    })
}