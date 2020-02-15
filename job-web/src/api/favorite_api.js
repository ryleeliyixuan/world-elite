import request from '@/utils/request'

export function doFavorite(data){
    return request({
        url: '/favorite/favorite',
        method: 'post',
        data
    })
}