package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class HomeConfigForm {
    private Integer configType; //配置类型
    private String mainTitle; //主标题
    private String subTitle; //副标题
    private String picUrl; //图片
    private String picName; //图片名
    private String target; //跳转链接
}
