package com.worldelite.job.util;

import cn.hutool.http.HtmlUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 对用户提交表单处理实用类
 *
 * @author yeguozhong yedaxia.github.com
 */
public class FormUtils {

    /**
     * 过滤所有网页标签
     *
     * @param value
     * @return
     */
    public static String removeAllHtmlTag(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return HtmlUtil.cleanHtmlTag(value);
    }

    /**
     * 过滤HTML文本，防止XSS攻击
     *
     * @param htmlContent
     * @return
     */
    public static String safeHtml(String htmlContent){
        if(StringUtils.isEmpty(htmlContent)){
            return null;
        }
        return HtmlUtil.filter(htmlContent);
    }

    /**
     *  拼接where 的id条件
     * @param ids
     * @return
     */
    public static String joinWhereIds(Integer[] ids){
        if(ids == null || ids.length == 0){
            return "";
        }
        return StringUtils.join(ids, ",");
    }
}