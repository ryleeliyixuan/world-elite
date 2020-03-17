import request from '@/utils/request'

export function getDownloadList(params) {
  return request({
    url: '/download/list',
    method: 'get',
    params
  })
}
