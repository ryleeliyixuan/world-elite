import request from '@/utils/request'

//post
export function savePost(data) {
  return request({
    url: '/company-post/save',
    method: 'post',
    data
  })
}

export function deletePost(postId) {
  return request({
    url: '/company-post/delete',
    method: 'post',
    params: { postId: postId }
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

export function deleteScore(scoreId) {
  return request({
    url: '/company-score/delete',
    method: 'post',
    params: { scoreId: scoreId }
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

export function deleteComment(commentId) {
  return request({
    url: '/company-comment/delete',
    method: 'post',
    params: { commentId: commentId }
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