import Toast from './toast'
import axios from 'axios'
import { getDownloadUrl } from '@/api/file_api'

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
export function linkName(link) {
  return link.replace(/http(s)?:\/\//, '');
}

/**
 * 完整链接
 */
export function wholeLink(link) {
  return link.startsWith("http") ? link : "http://" + link;
}

/**
 * 格式化查询条件，用于列表查询条件
 */
export function formatListQuery(listQuery) {
  const newQuery = {};
  Object.keys(listQuery).forEach(key => {
    if (listQuery[key]) {
      if (listQuery[key] instanceof Array) {
        if (listQuery[key].length !== 0) {
          newQuery[key] = listQuery[key].join(',')
        }
      } else {
        newQuery[key] = listQuery[key]
      }
    }
  });
  return newQuery;
}

/**
 * 把路由的查询条件映射到listQuery
 */
export function parseListQuery(query, listQuery) {
  if (query) {
    Object.keys(query).forEach(key => {
      // eslint-disable-next-line no-console
      if (query[key]) {
        if (listQuery[key].constructor == Array) {
          listQuery[key] = query[key].split(',').map(v => parseInt(v));
        } else if (listQuery[key].constructor == Number) {
          listQuery[key] = parseInt(query[key]);
        } else {
          listQuery[key] = query[key];
        }
      } else {
        listQuery[key] = query[key];
      }
    });
  }
}

/**
 * 图片大小是否在2Mb内
 */
export function checkPicSize(file) {
  const overSize = file.size / 1024 / 1024 > 2;
  if (overSize) {
    Toast.error('图片大小不能超过2Mb')
  }
  return overSize;
}

/**
 * 下载文件
 * fileKey: 对于服务器文件名
 * fileName: 下载显示文件名
 * success: 成功回调
 */
export function downloadFile(data) {
  const downloadFileUrl = getDownloadUrl({ fileKey: data.fileKey, fileName: data.fileName });
  axios.get(downloadFileUrl, {
    responseType: 'blob', //重要
  }).then((response) => {
    if (response.data.size < 50) {
      setTimeout(function () {
        downloadFile(data);
      }, 2000);
      return;
    }
    data.success();
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', data.fileName);
    document.body.appendChild(link);
    link.click();
  })
}