package com.worldelite.job.vo;

import lombok.Data;

/**
 * 视频Token视图对象
 */
@Data
public class RTCTokenVo{
    private String userId; //用户ID
    private String channelId; //频道ID
    private String appId; //应用ID
    private String nonce; //随机吗
    private String timestamp; //时间戳
    private String gslb; //gslb服务地址
    private String token; //令牌
}
