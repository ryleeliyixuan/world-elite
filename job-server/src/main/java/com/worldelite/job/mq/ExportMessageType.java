package com.worldelite.job.mq;

/**
 * @author yeguozhong yedaxia.github.com
 */
public interface ExportMessageType {

    /**
     * 导出用户列表
     */
    byte EXPORT_USER = 1;

    /**
     * 导出企业列表
     */
    byte EXPORT_COMPANY = 2;

    /**
     * 导出简历列表
     */
    byte EXPORT_RESUME = 3;

    /**
     * 导出职位列表
     */
    byte EXPORT_JOB = 4;
}
