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
        url: '/usercorporate/register',
        method: 'post',
        data
      })
}

export function checkEmailExists(email){
  return request({
    url: '/usercorporate/check-email',
    method: 'get',
    params: {email: email}
  })
}

export function login(data){
    return request({
        url: '/usercorporate/email-login',
        method: 'post',
        data
      })
}

export function resetPwd(data){
    return request({
        url: '/usercorporate/reset-pwd',
        method: 'post',
        data
      })
}

export function logout(){
    return request({
      url: '/usercorporate/logout',
      method: 'post'
    })
  }

  export function modifyPwd(data){
    return request({
      url: '/usercorporate/modify-pwd',
      method: 'post',
      data
    })
  }