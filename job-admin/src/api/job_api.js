import request from '@/utils/request'

export function getCompanyJobList(data) {
    return request({
        url: '/job/company-job-list',
        method: 'get',
        params: data
    })
}

export function getJobInfo(id) {
    return request({
        url: '/job/get-job-info',
        method: 'get',
        params: { id: id }
    })
}

export function applyJob(id) {
    return request({
        url: '/job/apply-job',
        method: 'post',
        params: { id: id }
    })
}

export function searchJob(data) {
    return request({
        url: '/job/search-job',
        method: 'post',
        data
    })
}

export function myApplyJobList(data) {
    return request({
        url: '/job/my-apply-jobs',
        method: 'get',
        data
    })
}

export function getJobList(data) {
    return request({
        url: '/job/list',
        method: 'get',
        params: data
    })
}

export function takeOffJob(data){
    return request({
        url: '/job/take-off-job',
        method: 'post',
        params: data
    })
}