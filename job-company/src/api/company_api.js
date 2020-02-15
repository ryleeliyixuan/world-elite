import request from '@/utils/request'

export function serachByName(name) {
  return request({
    url: '/company/search',
    method: 'get',
    params: { name: name, page: 1, limit: 10 }
  })
}

export function saveVerifyInfo(data) {
  return request({
    url: '/company/save-verify-info',
    method: 'post',
    data
  })
}

export function getVerifyInfo() {
  return request({
    url: '/company/my-verify-info',
    method: 'get',
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