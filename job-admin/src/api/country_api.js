import request from '@/utils/request'

export function getAllCountries() {
  return request({
    url: '/country/get-all',
    method: 'get'
  })
}

export function getCurrentCountry() {
  return request({
    url: '/country/current',
    method: 'get'
  })
}
