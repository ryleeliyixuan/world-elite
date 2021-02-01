import request from '@/utils/request'

export function saveHomeConfig(data){
    return request({
        url: '/config/save-home-config',
        method: 'post',
        data
      })
}

export function getHomeConfig() {
  return request({
    url: '/config/get-recommend-config?configType=11',
    method: 'get',
    params: {configType: 11}
  })
}

export function saveWebHomeConfig(data){
  return request({
      url: '/config/save-recommend-config',
      method: 'post',
      data
    })
}

export function getCarouselList(){
  return request({
    url: '/config/list-carousel',
    method: 'get',
    params:{sort:'position'}

  })
}

export function deleteCarousel(id){
  return request({
    url: '/config/delete-carousel',
    method: 'post',
    params: {id:id},
  })
}

export function moveUpCarousel(id){
  return request({
    url: '/config/move-up-carousel',
    method: 'post',
    params: {id:id},

  })
}
export function moveDownCarousel(id){
  return request({
    url: '/config/move-down-carousel',
    method: 'post',
    params: {id:id}
  })
}
export function addCarousel(data){
  return request({
    url: '/config/add-carousel',
    method: 'post',
    data
  })
}

