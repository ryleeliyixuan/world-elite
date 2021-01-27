import request from '@/utils/request'

export function getResumeInfo(){
    return request({
        url: '/resume/my-resume',
        method: 'get',
      })
}
export function parseAttachment(data){
    return request({
        url: '/resume/parse-attachment',
        method: 'post',
        data
    })
}

export function saveResumeBasic(data){
    return request({
        url: '/resume/save-resume-basic',
        method: 'post',
        data
      })
}

export function addResume(data){
    return request({
        url: '/resume/add-resume',
        method: 'post',
        data
    })
}

export function saveResumeEdu(data){
    return request({
        url: '/resume/save-resume-edu',
        method: 'post',
        data
      })
}

export function delResumeEdu(id){
    return request({
        url: '/resume/del-resume-edu',
        method: 'post',
        params: {id: id}
      })
}

export function saveResumeExp(data){
    return request({
        url: '/resume/save-resume-exp',
        method: 'post',
        data
      })
}

export function delResumeExp(id){
    return request({
        url: '/resume/del-resume-exp',
        method: 'post',
        params: {id: id}
      })
}

export function saveResumePractice(data){
    return request({
        url: '/resume/save-resume-practice',
        method: 'post',
        data
      })
}

export function delResumePractice(id){
    return request({
        url: '/resume/del-resume-practice',
        method: 'post',
        params: {id: id}
      })
}

export function saveResumeLanguage(data) {
    return request({
        url:'/resume/save-resume-language',
        method:'post',
        data
    })
}
export function delResumeLanguage(id) {
    return request({
        url:'/resume/del-resume-language',
        method:'post',
        params:{id: id}
    })
}

export function saveResumeAwards(data) {
    return request({
        url:'/resume/save-resume-certificate',
        method:'post',
        data
    })
}

export function delResumeAwards(id) {
    return request({
        url:'/resume/del-resume-certificate',
        method:'post',
        params:{id:id}
    })

}

export function saveResumeSkills(data){
  return request({
    url: '/resume/save-resume-skills',
    method: 'post',
    data
  })
}


export function getResumeDetail(id) {
  return request({
    url: '/resume/resume-detail',
    method: 'get',
    params: { id: id }
  })
}

export function delResumeAttachment(resumeId){
  return request({
    url: '/resume/del-resume-attachment',
    method: 'post',
    params: { resumeId: resumeId }
  })
}
export function delResume(id) {
    return request({
        url:'/resume/del-resume',
        method:'post',
        params:{ id : id }

    })

}