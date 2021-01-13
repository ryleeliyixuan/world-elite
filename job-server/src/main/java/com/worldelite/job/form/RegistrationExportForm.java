package com.worldelite.job.form;

import lombok.Data;

/**
 * 报名表导出表单
 */
@Data
public class RegistrationExportForm extends RegistrationListForm{
    private String nameFlag; //excel是否包含姓名字段，0=不包含，1=包含
    private String genderFlag; //性别
    private String phoneFlag; //手机号
    private String emailFlag; //邮件
    private String schoolFlag; //学校
    private String professionFlag; //专业
    private String gradeFlag; //年级
    private String educationFlag; //学历
    private String registrationPdfFlag; //是否导出pdf报名信息
    private String resumePdfFlag; //是否导出pdf简历信息
}
