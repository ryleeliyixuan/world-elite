const appName = 'World Elite'

/**
 * 设置浏览器标题
 */
export function setPageTitle(pageTitle) {
    document.title = pageTitle? `${pageTitle} - ${appName}`: appName;
}

