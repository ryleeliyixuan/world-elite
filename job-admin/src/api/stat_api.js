import request from '@/utils/request'

export function getWeekUserStat() {
  return request({
    url: '/admin/stat/get-week-user-stat',
    method: 'get',
  })
}
