package com.worldelite.job.constants;

/**
 * 阿里云OSS资源路径
 * @author yeguozhong yedaxia.github.com
 */
public enum OssDir {

    PIC("pic"),
    ATTACHMENT("attachment");

    public final String value;

    OssDir(String value) {
        this.value = value;
    }
}
