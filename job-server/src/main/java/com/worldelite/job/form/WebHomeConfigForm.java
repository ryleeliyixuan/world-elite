package com.worldelite.job.form;

import lombok.Data;

import java.util.List;

/**
 * 用户端首页配置信息表单
 */
@Data
public class WebHomeConfigForm {
    private Integer configType; //配置类型
    private List<String> carouselImages; //轮播图列表
    private String wikiImage; //百科图片
    private String mockImage; //mock图片
}
