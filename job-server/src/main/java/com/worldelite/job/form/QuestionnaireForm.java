package com.worldelite.job.form;

import lombok.Data;

/**
 * 报名表问题表单
 */
@Data
public class QuestionnaireForm {
    private Integer registrationTemplateId; //报名模板ID
    private String title; //问题
    private String mustAnswer; //必答
    private String type; //类型
    private Integer sort; //排序
    private Byte template; //是否为模板问题
    private QuestionnaireOptionsForm[] optionsList; //选项列表
}
