package com.worldelite.job.vo;

import lombok.Data;

import java.util.List;

/**
 * 导出报名表PDF内容视图
 */
@Data
public class AnswerContentPDFVo {
    private String content; //内容
    private Boolean isPicture; //是否是图片
}
