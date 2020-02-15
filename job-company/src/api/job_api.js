import request from '@/utils/request'

export function getJobInfo(id) {
    return request({
        url: '/job/get-job-info',
        method: 'get',
        params: { id: id }
    })
}

export function saveJob(data) {
    return request({
        url: '/job/save',
        method: 'post',
        data
    })
}

export function getManageJobList(data) {
    return request({
        url: '/job/manage-job-list',
        method: 'get',
        params: data
    })
}

export function takeOffJob(id) {
    return request({
        url: '/job/take-off-job',
        method: 'post',
        params: { id: id }
    })
}