package com.worldelite.job.form;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * 活动列表form管理端扩展
 *
 * @author Xiang Chao
 **/

@Data
public class ActivityListAdminForm extends ActivityListForm {

    private Integer id; //活动id
    private String title; //活动标题
    private String organizerName; //组织名
    private String organizerType; //组织类型
    private Integer reviewType; //审核类型.1=初次审核;2=编辑后审核
    private Long activityStartTime; //活动开始时间
    private Long activityFinishTime; //活动结束时间

    public Timestamp getActivityStartTime() {
        if (activityStartTime == null) return null;
        return Timestamp.from(Instant.ofEpochMilli(activityStartTime));
    }

    public Timestamp getActivityFinishTime() {
        if (activityFinishTime == null) return null;
        return Timestamp.from(Instant.ofEpochMilli(activityFinishTime));
    }
}

