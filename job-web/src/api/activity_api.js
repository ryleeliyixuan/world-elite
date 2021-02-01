import request from '@/utils/request'

export function getActivityList(data) {
    return request({
        url: '/activity/list',
        method: 'get',
        params: data
    })
}
