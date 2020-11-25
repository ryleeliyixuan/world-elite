import request from '@/utils/request'

export function addInterview(data){
    return request({
        url: 'https://api.test.myworldelite.com/mock/interviewer/info',
        method: 'post',
        data
    })
}

export function addInterviewDirection(data) {
    return request({
        url: 'https://api.test.myworldelite.com/mock/interviewer/direction',
        method: 'post',
        data
    })
}
export function addInterviewAuth(data) {
    return request({
        url: 'https://api.test.myworldelite.com/mock/interviewer/auth',
        method: 'post',
        data
    })
}