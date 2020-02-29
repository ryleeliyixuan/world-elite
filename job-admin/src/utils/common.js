
export function resizeImg(url, width) {
  return url + '?x-oss-process=image/resize,w_' + width
}
