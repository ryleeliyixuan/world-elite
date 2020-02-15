import request from '@/utils/request'

export function getCompanyJobList(data) {
    return request({
        url: '/job/company-job-list',
        method: 'get',
        params: data
    })
}

export function getJobInfo(id){
    return request({
        url: '/job/get-job-info',
        method: 'get',
        params: {id: id}
    })
}

export function applyJob(id){
    return request({
        url: '/job/apply-job',
        method: 'post',
        params: {id: id}
    })
}