package com.worldelite.job.form;

import lombok.Data;

/**
 * 报名表导出表单
 */
@Data
public class RegistrationExportForm extends RegistrationListForm{
    private String nameFlag; //excel是否包含姓名字段，0=不包含，1=包含
    private String genderFlag;
    private String phoneFlag;
    private String emailFlag;
    private String schoolFlag;
    private String professionFlag;
    private String gradeFlag;
    private String educationFlag;
    private String registrationPdfFlag;
    private String resumePdfFlag;
}
