import request from '@/utils/request'

export function getUploadPicToken(fileName) {
    return request({
      url: '/upload/pic-token',
      method: 'get',
      params: { fileName: fileName }
    })
  }

export function getUploadAttachmentToken(fileName) {
    return request({
        url: '/upload/attachment-token',
        method: 'get',
        params: { fileName: fileName }
    })
}
