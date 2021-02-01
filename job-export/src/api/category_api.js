import request from '@/utils/request'

export function getCategoryTree() {
    return request({
        url: '/category/category-tree',
        method: 'get'
    })
}