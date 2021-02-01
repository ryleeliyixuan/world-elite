import request from '@/utils/request'

export function serachByName(data) {
  return request({
    url: '/company/search',
    method: 'get',
    params: data
  })
}

export function getCompanyInfo(id) {
  return request({
    url: '/company/company-home-data',
    method: 'get',
    params: { id: id }
  })
}

export function getCompanyWiki(companyId) {
  return request({
    url: '/company/get-company-wiki',
    method: 'get',
    params: { companyId: companyId }
  })
}


