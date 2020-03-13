import request from '@/utils/request'

export function getEmailCode(email){
    return request({
        url: '/user/get-email-code',
        method: 'get',
        params: {email: email} 
      })
}

export function register(data) {
    return request({
        url: '/auth/register',
        method: 'post',
        data
      })
}

export function login(data){
    return request({
        url: '/auth/email-login',
        method: 'post',
        data
      })
}

export function resetPwd(data){
    return request({
        url: '/auth/reset-pwd',
        method: 'post',
        data
      })
}

export function logout(){
    return request({
      url: '/auth/logout',
      method: 'post'
    })
  }