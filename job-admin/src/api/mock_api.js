import request from '@/utils/request'

export function getInterviewerList(data) {
  return request({
    url: '/mock/interviewer/list',
    method: 'get',
    params: data
  })
}

export function getInterviewRecordList(data) {

}

export function getAvatar() {
  return request({
    url: '/mock/avatar',
    method: 'get'
  })
}
