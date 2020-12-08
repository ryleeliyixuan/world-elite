import request from '@/utils/request'


export function searchCommentInPost(data) {
  return request({
    url: '/company-comment/search-in-post',
    method: 'post',
    data
  })
}

export function searchPost(data) {
  return request({
    url: '/company-post/search',
    method: 'post',
    data
  })
}

export function searchScore(data) {
  return request({
    url: '/company-score/search',
    method: 'post',
    data
  })
}

export function searchReplyInScore(data) {
  return request({
    url: '/company-comment/search-in-score',
    method: 'post',
    data
  })
}

export function forbiddenUser(data) {
  return request({
    url: '/company-forbidden/forbidden',
    method: 'post',
    data
  })
}

export function unforbiddenUser(data) {
  return request({
    url: '/company-forbidden/un-forbidden',
    method: 'post',
    data
  })
}

export function forbiddenAllUser(data) {
  return request({
    url: '/company-forbidden/forbidden-all',
    method: 'post',
    data
  })
}

//post
export function savePost(data) {
  return request({
    url: '/company-post/save',
    method: 'post',
    data
  })
}

export function deletePost(data) {
  return request({
    url: '/company-post/delete',
    method: 'post',
    data
  })
}

export function deleteAll(data) {
  return request({
    url: '/company-post/delete-all',
    method: 'post',
    data
  })
}

export function likePost(postId) {
  return request({
    url: '/company-post/like',
    method: 'post',
    params: { postId: postId }
  })
}

export function reportPost(data) {
  return request({
    url: '/company-post/report',
    method: 'post',
    data
  })
}

export function commentPost(data) {
  return request({
    url: '/company-post/comment',
    method: 'post',
    data
  })
}

export function getPostList(data) {
  return request({
    url: '/company-post/list',
    method: 'post',
    data
  })
}

export function getPostDetail(postId) {
  return request({
    url: '/company-post/detail',
    method: 'get',
    params: { postId: postId }
  })
}

//rate score
export function saveScore(data) {
  return request({
    url: '/company-score/save',
    method: 'post',
    data
  })
}

export function deleteScore(data) {
  return request({
    url: '/company-score/delete',
    method: 'post',
    data
  })
}

export function deleteAllScore(data) {
  return request({
    url: '/company-score/delete-all',
    method: 'post',
    data
  })
}

export function likeScore(scoreId) {
  return request({
    url: '/company-score/like',
    method: 'post',
    params: { scoreId: scoreId}
  })
}

export function reportScore(data) {
  return request({
    url: '/company-score/report',
    method: 'post',
    data
  })
}

export function commentScore(data) {
  return request({
    url: '/company-score/comment',
    method: 'post',
    data
  })
}

export function getScoreList(data) {
  return request({
    url: '/company-score/list',
    method: 'post',
    data
  })
}

export function getMyScore(companyId) {
  return request({
    url: '/company-score/my-score-info',
    method: 'get',
    params: { companyId: companyId }
  })
}

//comment
export function getCommentList(data) {
  return request({
    url: '/company-comment/list',
    method: 'post',
    data
  })
}

export function deleteComment(data) {
  return request({
    url: '/company-comment/delete',
    method: 'post',
    data
  })
}

export function deleteAllComment(data) {
  return request({
    url: '/company-comment/delete-all',
    method: 'post',
    data
  })
}

export function likeComment(commentId) {
  return request({
    url: '/company-comment/like',
    method: 'post',
    params: { commentId: commentId }
  })
}

export function reportComment(data) {
  return request({
    url: '/company-comment/report',
    method: 'post',
    data
  })
}

export function replyComment(data) {
  return request({
    url: '/company-comment/comment',
    method: 'post',
    data
  })
}