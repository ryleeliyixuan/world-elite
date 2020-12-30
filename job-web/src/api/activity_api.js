import request from '@/utils/request'

export function getActivityList(data) {
    return request({
        url: '/activity/list',
        method: 'post',
        data
    })
}

export function getActivityInfo(id){
    return request({
        url: '/activity/activity-info',
        method: 'get',
        params: {id: id}
    })
}
