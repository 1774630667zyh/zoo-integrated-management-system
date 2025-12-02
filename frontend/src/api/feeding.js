import request from '@/utils/request'

// 获取某只动物的喂养计划
export function getPlansByAnimal(animalId) {
    return request({
        url: `/feeding-plans/animal/${animalId}`,
        method: 'get'
    })
}

// 新增喂养计划
export function createPlan(data) {
    return request({
        url: '/feeding-plans',
        method: 'post',
        data
    })
}

// 删除喂养计划
export function deletePlan(id) {
    return request({
        url: `/feeding-plans/${id}`,
        method: 'delete'
    })
}