import request from '@/utils/request'

export function doFavorite(data){
    return request({
        url: '/favorite/favorite',
        method: 'post',
        data
    })
}

export function getFavoriteList(data){
    return request({
        url: '/favorite/my-favorite-list',
        method: 'get',
        params: data
    })
}

export function getFavoriteActivityList(data){
    return request({
        url: '/favorite/my-favorite-activities',
        method: 'get',
        params: data
    })
}