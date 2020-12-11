import request from '@/utils/request'
import {retrieveColumnLayout} from "echarts/src/layout/barGrid";

export async function getWeekUserStat() {
  return request({
    url: '/admin/stat/get-week-user-stat',
    method: 'get',
  })
}

export async function getMonthUserStat() {
  return request({
    url: '/admin/stat/get-month-user-stat',
    method: 'get'
  })
}

export async function getSpecifyMonthUserStat(date) {
  return request({
    url: '/admin/stat/get-specify-month-user-stat',
    method: 'get',
    params: {
      date: date
    }
  })
}

export async function getSpecifyYearUserStat(date) {
  return request({
    url: '/admin/stat/get-specify-year-user-stat',
    method: 'get',
    params: {
      date: date
    }
  })
}

export function getDateList() {
  return request({
    url: '/admin/stat/get-date-list',
    method: 'get'
  })
}
