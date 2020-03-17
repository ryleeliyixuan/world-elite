import request from '@/utils/request'

export function getEmailCode(email) {
  return request({
    url: '/user/get-email-code',
    method: 'get',
    params: { email: email }
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function login(data) {
  return request({
    url: '/user/email-login',
    method: 'post',
    data
  })
}

export function getMyInfo() {
  return request({
    url: '/user/my-info',
    method: 'get'
  })
}

export function saveUserExpectJob(data) {
  return request({
    url: '/user/save-expect-job',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function getUserList(data) {
  return request({
    url: '/user/list',
    method: 'get',
    params: data
  })
}

export function modifyUserStatus(data) {
  return request({
    url: '/user/modify-status',
    method: 'post',
    data
  })
}

export function getCompanyUserList(data) {
  return request({
    url: '/user/company-user-list',
    method: 'get',
    params: data
  })
}