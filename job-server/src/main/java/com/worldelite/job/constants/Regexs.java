package com.worldelite.job.constants;

/**
 * @author yeguozhong yedaxia.github.com
 */
public interface Regexs {
    /**
     * 密码长度为8到20位,必须包含字母和数字
     */
    String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{8,20})$";
}
