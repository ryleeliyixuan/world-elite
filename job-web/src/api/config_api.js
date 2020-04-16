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

export function getUserProtocol(){
   return request({
    url: '/config/user-protocol',
    method: 'get'
  })
}
