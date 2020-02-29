import request from '@/utils/request'

export function saveVerifyInfo(data) {
    return request({
      url: '/verify/save-verify-info',
      method: 'post',
      data
    })
  }
  
  export function getVerifyInfo() {
    return request({
      url: '/verify/my-verify-info',
      method: 'get',
    })
  }