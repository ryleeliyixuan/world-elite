
export function resizeImg(url, width) {
  return url + '?x-oss-process=image/resize,w_' + width
}

/**
 * 获取当前相对路径
 */
export function curRelativePath() {
  const cur_url = window.location.href;
  let start_index = cur_url.indexOf('/', 8);
  let result_url = cur_url.substring(start_index, cur_url.length);
  if (result_url.charAt(1) == '#') {
      result_url = result_url.substring(2, result_url.length);
  }
  return result_url;
}

/**
* 去掉http部分
*/
export function linkName(link){
  return link.replace(/http(s)?:\/\//, '');
}

/**
* 完整链接
*/
export function wholeLink(link){
  return link.startsWith("http")? link : "http://" + link;
}
