package com.worldelite.job.context;

/**
 * @author yeguozhong yedaxia.github.com
 */
public interface RedisKeys {

    /**
     * 邮箱验证码
     */
    String VALIDATE_EMAIL_PREFIX = "validate.email.";

    /**
     * 职位类型树
     */
    String JOB_CATEGORY_TREE = "job_category_tree";

    /**
     * 用户在线状态信息
     */
    String ATTR_ONLINE_INFO = "ATTR_ONLINE_INFO";
}
