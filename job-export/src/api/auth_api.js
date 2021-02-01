import request from '@/utils/request'

export function getEmailCode(email){
    return request({
        url: '/auth/get-email-code',
        method: 'get',
        params: {email: email} 
      })
}

export function register(data) {
    return request({
        url: '/userapplicant/register',
        method: 'post',
        data
      })
}

export function checkEmailExists(email){
  return request({
    url: '/userapplicant/check-email',
    method: 'get',
    params: {email: email}
  })
}

export function bindAccount(data){
  return request({
    url: '/userapplicant/bind-account',
    method: 'post',
    data
  })
}

export function login(data){
    return request({
        url: '/userapplicant/email-login',
        method: 'post',
        data
      })
}

export function resetPwd(data){
    return request({
        url: '/userapplicant/reset-pwd',
        method: 'post',
        data
      })
}

export function modifyPwd(data){
  return request({
    url: '/userapplicant/modify-pwd',
    method: 'post',
    data
  })
}

export function logout(){
    return request({
      url: '/userapplicant/logout',
      method: 'post'
    })
  }