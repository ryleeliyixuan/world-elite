package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.ActivityReport;
import com.worldelite.job.entity.ActivityReportExt;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author Xiang Chao
 **/
@Data
public class ActivityReportVo implements VoConvertable<ActivityReportVo, ActivityReport> {

    private Integer id; //举报ID
    private Integer activityId; //活动ID
    private Long reportUserId; //举报者用户ID,留空为当前登录账户
    private DictVo optionVo; //举报理由选项Dictvo
    private String reason; //举报原因
    private String[] evidence; //证据图片url
    private Byte status; //举报状态, 0:审核中 1:驳回举报,2:下架活动. 管理端更新用
    private String result; //举报处理结果

    private String title; //活动标题
    private String organizerName; //组织名
    private String organizerType; //组织类型
    private Long activityStartTime; //活动开始时间
    private Long activityFinishTime; //活动结束时间
    private String reportUserName; //举报人名字
    private Long createTime;//举报时间

    @Override
    public ActivityReportVo asVo(ActivityReport activityReport) {
        if (activityReport == null) return null;
        BeanUtil.copyProperties(activityReport, this, "activityStartTime", "activityFinishTime", "createTime");

        if(StringUtils.isNotBlank(activityReport.getEvidence())){
            setEvidence(activityReport.getEvidence().split(";"));
        }

        if(activityReport instanceof ActivityReportExt) {
            ActivityReportExt reportExt = (ActivityReportExt) activityReport;
            if (reportExt.getActivityStartTime() != null)
                setActivityStartTime(reportExt.getActivityStartTime().getTime());

            if (reportExt.getActivityFinishTime() != null)
                setActivityFinishTime(reportExt.getActivityFinishTime().getTime());
        }
        if (activityReport.getCreateTime() != null)
            setCreateTime(activityReport.getCreateTime().getTime());
        return this;
    }
}
