import request from '@/utils/request'

export function getActivityList(data) {
  return request({
    url: '/activity/list',
    method: 'get',
    params: data
  })
}


export function saveActivity(data) {
  return request({
    url: '/activity/save',
    method: 'post',
    data
  })
}

export function takeoffActivity(id) {
  return request({
    url: '/activity/takeoff',
    method: 'post',
    params: { id: id }
  })
}

export function getActivityInfo(id) {
  return request({
    url: '/activity/activity-info',
    method: 'get',
    params: { id: id }
  })
}
