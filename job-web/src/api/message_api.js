import request from '@/utils/request'

export function getMessageList(data) {
    return request({
        url: '/message/list',
        method: 'get',
        data
    })
}

export function getUnReadMessageCount() {
    return request({
        url: '/message/unread-count',
        method: 'get'
    })
}

export function readAllMessage() {
    return request({
        url: '/message/read-all-message',
        method: 'post'
    })
}

export function deleteMessage(id){
    return request({
        url: '/message/delete',
        method: 'post',
        params: {id :id}
    })
}

export function deleteAllMessage(){
    return request({
        url: '/message/delete-all',
        method: 'post',
    })
}
