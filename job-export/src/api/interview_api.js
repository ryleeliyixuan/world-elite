import request from '@/utils/request'

export function addInterview(data) {
    return request({
        url: '/mock/interviewer/info',
        method: 'post',
        data
    })
}

export function addInterviewDirection(data) {
    return request({
        url: '/mock/interviewer/direction',
        method: 'post',
        data
    })
}

export function addInterviewAuth(data) {
    return request({
        url: '/mock/interviewer/auth',
        method: 'post',
        data
    })
}

export function getResumeInfo() {
    return request({
        url: '/mock/avatar',
        method: 'get',
    })
}

export function saveResumeBasic(data) {
    return request({
        url: '/mock/avatar',
        method: 'post',
        data
    })
}