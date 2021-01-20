package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Xiang Chao
 **/
@Data
public class ActivityReportListForm extends ActivityReportForm{
    private String title; //活动标题
    private String organizerName; //组织名
    private String organizerType; //组织类型
    private Long activityStartTime; //活动开始时间
    private Long activityFinishTime; //活动结束时间
    private String reportUserName; //举报人名字
    private String optionIds; //举报理由选项 1,2,3数组

    public Timestamp getActivityStartTime() {
        if (activityStartTime == null) return null;
        return Timestamp.from(Instant.ofEpochMilli(activityStartTime));
    }

    public Timestamp getActivityFinishTime() {
        if (activityFinishTime == null) return null;
        return Timestamp.from(Instant.ofEpochMilli(activityFinishTime));
    }
}
