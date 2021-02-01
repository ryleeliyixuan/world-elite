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

export function getCarouselList(){
  return request({
    url: '/config/list-carousel',
    method: 'get',
    params:{sort:'position'}

  })
}

export function getRcmdConfig() {
  return request({
    url: '/config/get-recommend-config?configType=11',
    method: 'get',
    params: {configType: 11}
  })
}