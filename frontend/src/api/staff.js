import request from '@/utils/request'

// 分页获取员工列表
export function getStaffList(params) {
    return request({
        url: '/staffs',
        method: 'get',
        params
    })
}

// 新增员工
export function createStaff(data) {
    return request({
        url: '/staffs',
        method: 'post',
        data
    })
}

// 修改员工信息 (如禁用/启用，修改角色)
export function updateStaff(id, data) {
    return request({
        url: `/staffs/${id}`,
        method: 'put',
        data
    })
}