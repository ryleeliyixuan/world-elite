import request from '@/utils/request'

export function searchSchool(keyword) {
  return request({
    url: '/school/search',
    method: 'get',
    params: { keyword: keyword }
  })
}

export function getSchoolList(params){
  return request({
    url: '/school/list',
    method: 'get',
    params: params
  })
}

export function saveSchool(data){
  return request({
    url: '/school/save',
    method: 'post',
    data
  })
}
