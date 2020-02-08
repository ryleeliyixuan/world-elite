import request from '@/utils/request'

export function serachByName(name){
    return request({
        url: '/company/search',
        method: 'get',
        params: {name: name, page:1, limit:10}
      })
}