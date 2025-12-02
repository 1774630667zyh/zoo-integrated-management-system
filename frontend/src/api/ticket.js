import request from '@/utils/request'

export function getTicketList(params) {
    return request({
        url: '/tickets/list',
        method: 'get',
        params
    })
}

export function createOrder(data) {
    return request({
        url: '/tickets/order',
        method: 'post',
        data
    })
}