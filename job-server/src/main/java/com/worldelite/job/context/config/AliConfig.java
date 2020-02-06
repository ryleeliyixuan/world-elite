package com.worldelite.job.context.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Component
@Data
public class AliConfig {

    @Value("${oss.endPoint}")
    private String endPoint;

    @Value("${aliyun.region}")
    private String region;

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    /**
     * 上传token过期时间，单位是秒
     */
    @Value("${oss.uploadTokenExpired}")
    private Long uploadTokenExpired;

    /**
     * 上传图片大小限制：单位是Mb
     */
    @Value("${oss.uploadMaxPicSize}")
    private Long uploadMaxPicSize;
}
