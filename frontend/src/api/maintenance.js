import request from '@/utils/request'

// 分页获取工单列表
export function getMaintenanceList(params) {
    return request({
        url: '/maintenance',
        method: 'get',
        params
    })
}

// 提交报修
export function reportMaintenance(data) {
    return request({
        url: '/maintenance',
        method: 'post',
        data
    })
}

// 完成维修
export function completeMaintenance(id, result) {
    return request({
        url: `/maintenance/${id}/complete`,
        method: 'put',
        data: { result }
    })
}