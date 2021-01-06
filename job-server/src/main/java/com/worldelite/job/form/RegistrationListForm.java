package com.worldelite.job.form;

import lombok.Data;

/**
 * 活动报名查询表单
 */
@Data
public class RegistrationListForm extends PageForm{
    private Integer activityId; //活动ID
    private Long userId; //活动发布者ID
    private Long registrationUserId; //活动报名者ID
    private Integer[] statusList; //活动状态列表
    private String keywords; //关键词
}
