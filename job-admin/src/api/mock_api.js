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

export function addInterviewDirection(data) {
  return request({
    url: '/mock/interviewer/direction',
    method: 'post',
    data
  })
}

export function addInterviewAuth(data) {
  return request({
    url: '/mock/interviewer/auth',
    method: 'post',
    data
  })
}

export function getInterviewerInfo(id){
  return request({
    url: '/mock/interviewer/'+id,
    method: 'get'
  })
}

export function getInterviewDirection(id){
  return request({
    url: '/mock/interviewer/direction/'+id,
    method: 'get'
  })
}

export function getInterviewerBaseInfo(id){
  return request({
    url: '/userapplicant/user-info',
    method: 'get',
    params: {userId: id}
  })
}

export function getInterviewerTimeInfo(data){
  return request({
    url: '/mock/interview/time/query',
    method: 'post',
    data
  })
}
