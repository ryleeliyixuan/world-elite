import request from '@/utils/request'

export function saveRecommend(data){
    return request({
        url: '/recommend/save',
        method: 'post',
        data
      })
}

export function delRecommend(id){
    return request({
        url: '/recommend/delete',
        method: 'post',
        params: {id: id}
      })
}

export function getRecommendList(params){
    return request({
        url: '/recommend/list',
        method: 'get',
        params: params
      })
}