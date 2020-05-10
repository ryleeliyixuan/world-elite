import request from '@/utils/request'

export function serachByName(name) {
  return request({
    url: '/company/search',
    method: 'get',
    params: { name: name, page: 1, limit: 10 }
  })
}

export function myCompanyInfo(){
  return request({
    url: '/company/my-company-info',
    method: 'get',
  })
}

export function saveCompany(data){
  return request({
    url: '/company/save-company',
    method: 'post',
    data
  })
}

export function saveCompanyAddr(data){
  return request({
    url: '/company/save-company-addr',
    method: 'post',
    data
  })
}

export function delCompanyAddr(id){
  return request({
    url: '/company/del-company-addr',
    method: 'post',
    params: {id: id}
  })
}

export function getCompanyAddrList(){
  return request({
    url: '/company/list-my-company-addr',
    method: 'get'
  })
}

export function getCompanyInfo(id){
  return request({
    url: '/company/company-home-data',
    method: 'get',
    params: {id: id}
  })
}
