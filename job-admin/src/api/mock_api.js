import request from '@/utils/request'

export function getInterviewerList(data) {
  return request({
    url: '/mock/interviewer/admin/list',
    method: 'get',
    params: data
  })
}

export function getInterviewRecordList(data) {
  return request({
    url: '/mock/interview/records/query',
    method: 'post',
    data
  })
}

export function getAvatar() {
  return request({
    url: '/mock/avatar',
    method: 'get'
  })
}

export function addInterview(data) {
  return request({
    url: '/mock/interviewer/info',
    method: 'post',
    data
  })
}

export function updateInterview(data) {
  return request({
    url: '/mock/interviewer/info',
    method: 'patch',
    data
  })
}

export function addInterviewDirection(data) {
  return request({
    url: '/mock/interviewer/direction',
    method: 'post',
    data
  })
}

export function updateInterviewDirection(data) {
  return request({
    url: '/mock/interviewer/direction',
    method: 'patch',
    data
  })
}

export function delInterviewerInfo(id) {
  return request({
    url: '/mock/interviewer/direction/' + id,
    method: 'delete'
  })
}

export function addInterviewAuth(data) {
  return request({
    url: '/mock/interviewer/auth',
    method: 'post',
    data
  })
}

export function updateInterviewAuth(data) {
  return request({
    url: '/mock/interviewer/auth/',
    method: 'patch',
    data
  })
}

export function getInterviewerInfo(id) {
  return request({
    url: '/mock/interviewer/' + id,
    method: 'get'
  })
}

export function getInterviewDirection(id) {
  return request({
    url: '/mock/interviewer/direction/' + id,
    method: 'get'
  })
}

export function getInterviewerBaseInfo(id) {
  return request({
    url: '/userapplicant/user-info',
    method: 'get',
    params: {userId: id}
  })
}

export function getInterviewerTimeInfo(data) {
  return request({
    url: '/mock/interview/time/page',
    method: 'get',
    params: data
  })
}

//面试官下架
export function offShelfInterviewer(id) {
  return request({
    url: '/mock/interviewer/offshelf/' + id,
    method: 'patch'
  })
}


export function getInterviewAuth(id) {
  return request({
    url: '/mock/interviewer/auth/' + id,
    method: 'get',
  })
}
