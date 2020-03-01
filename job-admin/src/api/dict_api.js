import request from '@/utils/request'

export function getDictList(params) {
    return request({
      url: '/dict/list',
      method: 'get',
      params
    })
  }
  
  export function saveDict(data) {
    return request({
      url: '/dict/save',
      method: 'post',
      data
    })
  }
  
  export function deleteDict(id) {
    return request({
      url: '/dict/delete',
      method: 'post',
      params: { id: id }
    })
  }

export function listByType(type) {
  return request({
    url: '/dict/list',
    method: 'get',
    params: {
      type: type,
      page: 1,
      limit: 100,
      sort: '+id'
    }
  })
}
