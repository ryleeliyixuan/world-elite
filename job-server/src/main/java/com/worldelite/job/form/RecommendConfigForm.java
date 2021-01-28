package com.worldelite.job.form;

import lombok.Data;

/**
 * 用户端首页右侧推荐板块图片配置信息表单
 */
@Data
public class RecommendConfigForm {
    private Integer configType; //配置类型
    private String topPicUrl; //上方图片地址
    private String topPicName; //上方图片名
    private String topPicTarget; //上方图片跳转链接
    private String bottomPicUrl; //下方图片地址
    private String bottomPicName; //下方图片名
    private String bottomPicTarget; //下方图片跳转链接
}
