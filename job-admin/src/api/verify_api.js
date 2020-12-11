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
    params: {userId: userId}
  })
}

/**
 * 获取指定面试官的认证审核信息
 * @param userId 面试官id
 */
export function getInterviewerVerifyInfo(userId) {
  return request({
    url: '/mock/interviewer/auth/' + userId,
    method: 'get'
  })
}

/**
 * 审核面试官
 */
export function verifyInterviewer(data) {
  return request({
    url: '/mock/interviewer/auth/review',
    method: 'patch',
    params: {id: data.id, status: data.status}
  })
}

