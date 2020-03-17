import request from '@/utils/request'

export function exportResumeToPdf(resumeId) {
    return request({
        url: '/export/resume-to-pdf',
        method: 'get',
        params: {
            resumeId: resumeId
        }
    })
}

export function exportUserList(data){
    return request({
        url: '/export/export-user-list',
        method: 'post',
        data
    })
}

export function exportResumeList(data){
    return request({
        url: '/export/export-resume-list',
        method: 'post',
        data
    })
}
