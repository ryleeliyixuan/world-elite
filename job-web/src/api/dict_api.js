import request from '@/utils/request'

export function listByType(type){
    return request({
        url: '/dict/list',
        method: 'get',
        params: {
            type: type,
            page: 1,
            limit: 10,
            sort: '-value'
        }
    })
}

export function listByTypeWithSort(type, sort){
    return request({
        url: '/dict/list',
        method: 'get',
        params: {
            type: type,
            page: 1,
            limit: 10,
            sort: sort
        }
    })
}

export function listByTypeSalary(type){
    return request({
        url: '/dict/list',
        method: 'get',
        params: {
            type: type,
            page: 1,
            limit: 10,
            sort: 'id'
        }
    })
}


export function listByTypeAll(type){
    return request({
        url: '/dict/list',
        method: 'get',
        params: {
            type: type,
            page: 1,
            sort: '+id'
        }
    })
}