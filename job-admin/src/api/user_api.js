import request from '@/utils/request'

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function getEmailCode(email) {
  return request({
    url: '/user/get-email-code',
    method: 'get',
    params: { email: email }
  })
}

export function login(data) {
  return request({
    url: '/admin/email-login',
    method: 'post',
    data
  })
}

export function getMyInfo() {
  return request({
    url: '/admin/my-info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

export function getUserList(data) {
  return request({
    url: '/admin/list',
    method: 'get',
    params: data
  })
}

export function modifyApplicantStatus(data) {
  return request({
    url: '/admin/modify-applicant-status',
    method: 'post',
    data
  })
}

export function modifyCorporateStatus(data) {
  return request({
    url: '/admin/modify-corporate-status',
    method: 'post',
    data
  })
}

export function getCompanyUserList(data) {
  return request({
    url: '/admin/company-user-list',
    method: 'get',
    params: data
  })
}

export function getAdminUserList(data) {
  return request({
    url: '/admin/admin-list',
    method: 'get',
    params: data
  })
}

export function addAdmin(data){
  return request({
    url: '/admin/add-admin',
    method: 'post',
    data
  })
}

export function deleteAdmin(userId){
  return request({
    url: '/admin/delete-admin',
    method: 'post',
    params: {userId: userId}
  })
}

export function getUserRealNameAuthList(data) {
  return request({
    url: '/realnameauth',
    method: 'get',
    params: data
  })
}

export function realNameAuthFailure(userId, reason){
  return request({
    url: '/realnameauth/failure',
    method: 'patch',
    params:  {userId: userId, reason: reason}
  })
}

export function realNameAuthPass(userId){
  return request({
    url: '/realnameauth/pass',
    method: 'patch',
    params: {userId: userId}
  })
}

