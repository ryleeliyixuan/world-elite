import request from '@/utils/request'

export function saveHomeConfig(data){
    return request({
        url: '/config/save-home-config',
        method: 'post',
        data
      })
}

export function getHomeConfig(configType){
    return request({
        url: '/config/get-home-config',
        method: 'get',
        params: {configType: configType}
      })
}

export function saveWebHomeConfig(data){
  return request({
      url: '/config/save-web-home-config',
      method: 'post',
      data
    })
}

export function getWebHomeConfig(configType){
  return request({
      url: '/config/get-web-home-config',
      method: 'get',
      params: {configType: configType}
    })
}
