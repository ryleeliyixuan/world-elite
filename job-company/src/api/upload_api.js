import request from '@/utils/request'

export function getUploadPicToken(fileName) {
    return request({
      url: '/upload/pic-token',
      method: 'get',
      params: { fileName: fileName }
    })
  }