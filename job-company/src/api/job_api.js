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

export function opSaveJob(data) {
    return request({
        url: '/job/op-save',
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

export function deleteJob(data){
    return request({
        url: '/job/delete',
        method: 'post',
        params: data
    })
}


export function getUserJobOptions(){
    return request({
        url: '/job/user-job-options',
        method: 'get',
    })
}

export function reopenJob(id){
    return request({
        url: '/job/open-job',
        method: 'post',
        params: {id: id}
    })
}

export function getCompanyJobList(data) {
    return request({
        url: '/job/company-job-list',
        method: 'get',
        params: data
    })
}

export function getJobRecommendResumes(jobId){
    return request({
        url: '/job/recommend-resumes',
        method: 'get',
        params: {jobId: jobId}
    })
}