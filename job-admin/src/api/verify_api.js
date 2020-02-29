import request from '@/utils/request'

export function verifyCompanyUser(data) {
  return request({
    url: '/verify/verify-company-user',
    method: 'post',
    data
  })
}

export function getUserVerifyInfo(userId) {
  return request({
    url: '/verify/get-verify-info',
    method: 'get',
    params: { userId: userId }
  })
}
