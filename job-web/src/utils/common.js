/**
 * select 的不限值
 */
export const OPTION_UNLIMIT = -20201222;

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

/**
 * 格式化查询条件，用于列表查询条件
 */
export function formatListQuery(listQuery){
   const newQuery = {}; 
   Object.keys(listQuery).forEach(key => {
         if(listQuery[key]){
            if(listQuery[key] instanceof Array){
                if(listQuery[key].length !== 0){
                    newQuery[key] = listQuery[key].join(',')
                }
            }else{
                newQuery[key] = listQuery[key]
            }
         }
   });
   return newQuery;
}

/**
 * 把路由的查询条件映射到listQuery
 */
export function parseListQuery(query, listQuery){
    if(query){
        Object.keys(query).forEach(key => {
            // eslint-disable-next-line no-console
            if(listQuery[key]){
                if(listQuery[key].constructor == Array){
                    listQuery[key] = query[key].split(',').map(v => parseInt(v));
                }else if(listQuery[key].constructor == Number){
                    listQuery[key] = parseInt(query[key]);
                }else{
                    listQuery[key] = query[key];
                }
            }else{
                listQuery[key] = query[key];
            }
        });
    }
}