import request from '@/utils/request'

export function serachByCompanyName(name) {
  return request({
    url: '/company/search',
    method: 'get',
    params: { name: name, page: 1, limit: 10 }
  })
}

export function getCompanyList(data) {
  return request({
    url: '/company/list',
    method: 'get',
    params: data
  })
}

export function saveCompany(data) {
  return request({
    url: '/company/save-company',
    method: 'post',
    data
  })
}

export function saveCompanyAddr(data) {
  return request({
    url: '/company/save-company-addr',
    method: 'post',
    data
  })
}

export function delCompanyAddr(id) {
  return request({
    url: '/company/del-company-addr',
    method: 'post',
    params: { id: id }
  })
}

export function getCompanyAddrList() {
  return request({
    url: '/company/list-my-company-addr',
    method: 'get'
  })
}

export function getCompanyInfo(id){
  return request({
    url: '/company/get-company-info',
    method: 'get',
    params: {id: id}
  })
}

export function delCompany(id){
  return request({
    url: '/company/del-company',
    method: 'post',
    params: {id: id}
  })
}

export function getCompanyWiki(companyId){
  return request({
    url: '/company/get-company-wiki',
    method: 'get',
    params: {companyId: companyId}
  })
}

export function saveCompanyWiki(data){
  return request({
    url: '/company/save-company-wiki',
    method: 'post',
    data
  })
}
