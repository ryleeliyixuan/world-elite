import request from '@/utils/request'

export function getResumeInfo() {
  return request({
    url: '/resume/my-resume',
    method: 'get',
  })
}

export function saveResumeBasic(data) {
  return request({
    url: '/resume/save-resume-basic',
    method: 'post',
    data
  })
}

export function saveResumeEdu(data) {
  return request({
    url: '/resume/save-resume-edu',
    method: 'post',
    data
  })
}

export function delResumeEdu(id) {
  return request({
    url: '/resume/del-resume-edu',
    method: 'post',
    params: { id: id }
  })
}

export function saveResumeExp(data) {
  return request({
    url: '/resume/save-resume-exp',
    method: 'post',
    data
  })
}

export function delResumeExp(id) {
  return request({
    url: '/resume/del-resume-exp',
    method: 'post',
    params: { id: id }
  })
}

export function saveResumePractice(data) {
  return request({
    url: '/resume/save-resume-practice',
    method: 'post',
    data
  })
}

export function delResumePractice(id) {
  return request({
    url: '/resume/del-resume-practice',
    method: 'post',
    params: { id: id }
  })
}

export function saveResumeSkills(data) {
  return request({
    url: '/resume/save-resume-skills',
    method: 'post',
    data
  })
}

export function saveResumeLink(data) {
  return request({
    url: '/resume/save-resume-link',
    method: 'post',
    data
  })
}

export function delResumeLink(id) {
  return request({
    url: '/resume/del-resume-link',
    method: 'post',
    params: { id: id }
  })
}