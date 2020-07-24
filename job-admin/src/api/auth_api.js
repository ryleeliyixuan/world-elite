import request from '@/utils/request'

export function getEmailCode(email){
    return request({
        url: '/auth/get-email-code',
        method: 'get',
        params: {email: email} 
      })
}

export function login(data){
    return request({
        url: '/admin/email-login',
        method: 'post',
        data
      })
}

export function modifyPwd(data){
  return request({
    url: '/admin/modify-pwd',
    method: 'post',
    data
  })
}