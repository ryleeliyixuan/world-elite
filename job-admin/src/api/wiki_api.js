import request from '@/utils/request'

//market
export function saveCompanyMarket(data) {
  return request({
    url: '/company/save-company-market',
    method: 'post',
    data
  })
}

export function delCompanyMarket(marketId) {
  return request({
    url: '/company/delete-company-market',
    method: 'post',
    params: { marketId: marketId }
  })
}

export function saveCompanyHistory(data) {
  return request({
    url: '/company/save-company-history',
    method: 'post',
    data
  })
}

export function delCompanyHistory(historyId) {
  return request({
    url: '/company/delete-company-history',
    method: 'post',
    params: { historyId: historyId }
  })
}

export function saveCompanyEmployee(data) {
  return request({
    url: '/company/save-company-employee',
    method: 'post',
    data
  })
}

export function delCompanyEmployee(employeeId) {
  return request({
    url: '/company/delete-company-employee',
    method: 'post',
    params: { employeeId: employeeId }
  })
}

export function saveCompanyProduct(data) {
  return request({
    url: '/company//save-company-product',
    method: 'post',
    data
  })
}

export function delCompanyProduct(productId) {
  return request({
    url: '/company/delete-company-product',
    method: 'post',
    params: { productId: productId }
  })
}

export function saveCompanyStructure(data) {
  return request({
    url: '/company//save-company-structure',
    method: 'post',
    data
  })
}

export function delCompanyStructure(structureId) {
  return request({
    url: '/company/delete-company-structure',
    method: 'post',
    params: { structureId: structureId }
  })
}

export function listCompanyStructure(companyId) {
  return request({
    url: '/company/list-company-structure',
    method: 'get',
    params: { companyId: companyId }
  })
}