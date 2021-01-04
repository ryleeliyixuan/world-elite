package com.worldelite.job.form;

import lombok.Data;

/**
 * 报名表表单
 */
@Data
public class RegistrationForm {

    private Long registrationUserId; //报名者ID
    private Integer activityId; //活动ID
    private String name; //姓名
    private String gender; //性别
    private String phone; //手机号
    private String email; //邮箱
    private String school; //学校
    private String grade; //年级
    private String profession; //专业
    private String education; //学历

    private QuestionnaireAnswerForm[] answerList; //回答列表

    private Long resumeId; //简历ID

}
