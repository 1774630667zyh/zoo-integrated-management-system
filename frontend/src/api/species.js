import request from '@/utils/request'

export function getAllSpecies() {
    return request({
        url: '/species/list',
        method: 'get'
    })
}