package com.worldelite.job.vo;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UploadTokenVo {
    private String OSSAccessKeyId;
    private String policy;
    private String signature;
    private String expire;
    private String host;
    private String key;
    private String success_action_status = "200";
}
