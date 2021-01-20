package com.worldelite.job.form;

import lombok.Data;

/**
 * 从报名表添加模板表单
 */
@Data
public class AddTemplateFromQuestionnaireForm {
    private Integer activityQuestionnaireId; //报名表ID
    private String templateName; //模板名
}
