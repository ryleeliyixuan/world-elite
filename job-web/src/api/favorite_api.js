import request from '@/utils/request'

export function doFavorite(data){
    return request({
        url: '/favorite/favorite',
        method: 'post',
        data
    })
}

export function getFavoriteJobList(data){
    return request({
        url: '/favorite/get-favorite-jobs',
        method: 'get',
        params: data
    })
}