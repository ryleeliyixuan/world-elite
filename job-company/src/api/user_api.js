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

export function login(data){
    return request({
        url: '/usercorporate/email-login',
        method: 'post',
        data
      })
}

export function getMyInfo(){
  return request({
    url: '/usercorporate/my-info',
    method: 'get',
  })
}

export function saveUserExpectJob(data){
  return request({
    url: '/usercorporate/save-expect-job',
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