import request from '@/utils/request'

export function getResumeInfo(resumeId) {
    return request({
        url: '/resume-repository/resume',
        method: 'get',
        params: {resumeId: resumeId}
    })
}

export function saveResumeBasic(data) {
    return request({
        url: '/resume-repository/save-resume-basic',
        method: 'post',
        data
    })
}

export function saveResumeEdu(data) {
    return request({
        url: '/resume-repository/save-resume-edu',
        method: 'post',
        data
    })
}

export function delResumeEdu(id) {
    return request({
        url: '/resume-repository/del-resume-edu',
        method: 'post',
        params: {id: id}
    })
}

export function saveResumeExp(data) {
    return request({
        url: '/resume-repository/save-resume-exp',
        method: 'post',
        data
    })
}

export function delResumeExp(id) {
    return request({
        url: '/resume-repository/del-resume-exp',
        method: 'post',
        params: {id: id}
    })
}

export function saveResumePractice(data) {
    return request({
        url: '/resume-repository/save-resume-practice',
        method: 'post',
        data
    })
}

export function delResumePractice(id) {
    return request({
        url: '/resume-repository/del-resume-practice',
        method: 'post',
        params: {id: id}
    })
}

export function saveResumeSkills(data) {
    return request({
        url: '/resume-repository/save-resume-skills',
        method: 'post',
        data
    })
}

export function saveResumeLink(data) {
    return request({
        url: '/resume-repository/save-resume-link',
        method: 'post',
        data
    })
}

export function delResumeLink(id) {
    return request({
        url: '/resume-repository/del-resume-link',
        method: 'post',
        params: {id: id}
    })
}

export function delResumeAttachment(resumeId) {
    return request({
        url: '/resume-repository/del-resume-attachment',
        method: 'post',
        params: {resumeId: resumeId}
    })
}


// export function deleteNote(id) {
//     return request({
//         url: '/usercorporate/comment/delete',
//         method: 'post',
//         params: {userCorporateCommentId: id}
//     })
// }
//
export function applyResumeList(data) {
    return request({
        url: '/resume/my-apply-resume-list',
        method: 'post',
        data
    })
}
//
// export function applyResumeListOnline(data) {
//     return request({
//         url: '/resume/list',
//         method: 'post',
//         data
//     })
// }

// export function applyResumeListAttachment(data) {
//     return request({
//         url: '/resume/list-by-attachment',
//         method: 'post',
//         data
//     })
// }
//
// export function handleApplyResume(data) {
//     return request({
//         url: '/resume/handle-apply-resume',
//         method: 'post',
//         data
//     })
// }
//
// export function getResumeDetail(id) {
//     return request({
//         url: '/resume/resume-detail',
//         method: 'get',
//         params: {id: id}
//     })
// }

export function getResumeDetail(id) {
    return request({
        url: '/resume-repository/resume',
        method: 'get',
        params: {resumeId: id}
    })
}

export function saveUserExpectJob(data){
    return request({
        url: '/resume-repository/save-expect-job',
        method: 'post',
        data
    })
}
