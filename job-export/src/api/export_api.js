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