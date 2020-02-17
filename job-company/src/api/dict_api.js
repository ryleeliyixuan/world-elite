import request from '@/utils/request'

export function listByType(type){
    return request({
        url: '/dict/list',
        method: 'get',
        params: {
            type: type,
            page: 1,
            limit: 500,
            sort: '+id'
        } 
      })
}