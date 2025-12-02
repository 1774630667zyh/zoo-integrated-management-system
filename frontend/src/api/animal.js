import request from '@/utils/request'

export function getAnimalList(params) {
    return request({
        url: '/animals',
        method: 'get',
        params
    })
}

export function getAnimal(id) {
    return request({
        url: `/animals/${id}`,
        method: 'get'
    })
}

export function createAnimal(data) {
    return request({
        url: '/animals',
        method: 'post',
        data
    })
}

export function updateAnimal(id, data) {
    return request({
        url: `/animals/${id}`,
        method: 'put',
        data
    })
}

export function deleteAnimal(id) {
    return request({
        url: `/animals/${id}`,
        method: 'delete'
    })
}