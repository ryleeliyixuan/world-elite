package com.worldelite.job.form;

import lombok.Data;

@Data
public class CompanyForbiddenForm {
    private Long userId; //要禁言或解禁的用户ID
    private Long[] userIds; //要批量禁言或解禁的用户ID列表
    private String content; //禁言理由
    private Integer daysId; //禁言天数
    private Byte notice; //是否通知用户
}
