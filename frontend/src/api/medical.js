import request from '@/utils/request'

// 获取某只动物的病历历史
export function getMedicalHistory(animalId, params) {
    return request({
        url: `/medical/history/${animalId}`,
        method: 'get',
        params
    })
}

// 录入新病历
export function createMedicalRecord(data) {
    return request({
        url: '/medical',
        method: 'post',
        data
    })
}