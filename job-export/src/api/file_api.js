import { getToken } from '@/utils/auth'

/**
 * 获取下载链接
 */
export function getDownloadUrl(data){
   return `${process.env.VUE_APP_BASE_API}/file/download/${data.fileKey}?fileName=${data.fileName}&_token=${getToken()}`
}