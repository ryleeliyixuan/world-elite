import request from '@/utils/request'

export function getActivityList(data) {
    return request({
        url: '/activity/admin/list',
        method: 'get',
        params: data
    })
}


export function saveActivity(data) {
    return request({
        url: '/activity/save',
        method: 'post',
        data
    })
}

export function deleteActivity(id) {
    return request({
        url: '/activity/delete',
        method: 'post',
        params: {id: id}
    })
}

export function takeoffActivity(data) {
    return request({
        url: '/activity/takeoff',
        method: 'post',
        params: data
    })
}

export function getActivityInfo(id) {
    return request({
        url: '/activity/activity-info',
        method: 'get',
        params: {id: id}
    })
}

export function getActivityReviewInfo(id) {
    return request({
        url: '/activity/review/activity/'+ id,
        method: 'get',
    })
}

export function activityReviewPass(id) {
    return request({
        url: '/activity/review/pass/' + id,
        method: 'patch'
    })
}

export function activityReviewFailure(id, reason) {
    return request({
        url: '/activity/review/failure',
        method: 'patch',
        params: {activityId: id, reason: reason}
    })
}
