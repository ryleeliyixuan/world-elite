import request from '@/utils/request'

export function serachByName(name){
    return request({
        url: '/company/search',
        method: 'get',
        params: {name: name, page:1, limit:10}
      })
}

export function getCompanyInfo(id){
  return request({
    url: '/company/company-home-data',
    method: 'get',
    params: {id: id}
  })
}

export function getCompanyWikiList(data){
  return request({
    // url: '/company/wiki-list',
    url: '/company/list-wiki-by-industry',
    method: 'get',
    params: data
  })
}

export function getCompanyWiki(companyId){
  return request({
    url: '/company/get-company-wiki',
    method: 'get',
    params: {companyId: companyId}
  })
}


