import request from '@/utils/request'

export function searchSchool(keyword){
    return request({
        url: '/school/search',
        method: 'get',
        params: {keyword: keyword}
      })
}
