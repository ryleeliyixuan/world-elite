package com.worldelite.job.form;

import lombok.Data;

/**
 * 报名表模板添加/修改表单
 */
@Data
public class QuestionnaireTemplateForm {
    private String templateName; //模板名称
    private String title; //标题
    private String nameFlag; //姓名
    private String genderFlag; //性别
    private String phoneFlag; //手机号
    private String emailFlag; //邮箱
    private String schoolFlag; //学校
    private String gradeFlag; //年级
    private String professionFlag; //专业
    private String educationFlag; //学历
    private QuestionnaireForm[] questionnaireList; //问题列表
}
