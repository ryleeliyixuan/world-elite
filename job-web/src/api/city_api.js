import request from '@/utils/request'

export function getCityIdByName(names){
    return request({
        url: '/city/get-city-id-by-name',
        method: 'post',
        params: {cityNames: names}
    })
}