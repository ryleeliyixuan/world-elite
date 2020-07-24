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

export function login(data){
    return request({
        url: '/userapplicant/email-login',
        method: 'post',
        data
      })
}

export function getMyInfo(){
  return request({
    url: '/userapplicant/my-info',
    method: 'get',
  })
}

export function saveUserExpectJob(data){
  return request({
    url: '/userapplicant/save-expect-job',
    method: 'post',
    data
  })
}

export function modifyEmail(data){
  return request({
    url: '/userapplicant/modify-email',
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