package com.worldelite.job.form;

import lombok.Data;

/**
 * @author Xiang Chao
 **/
@Data
public class RealNameAuthForm extends PageForm {
    private Long userId; //记录id
    private String name; //姓名
    private String idNumber;//身份证号
    private String faceUrl;//身份证/学生证人像面
    private String emblemUrl;//身份证国徽面/学生证日期面
    private String holdUrl;//手持身份证/学生证正面照
    private Byte status;//审核状态.1:审核中,2:通过,3拒绝
    private Byte businessType;//实名认证业务类型.1:活动
}
