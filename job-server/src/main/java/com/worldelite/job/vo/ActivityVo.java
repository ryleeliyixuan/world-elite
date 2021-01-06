package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ActivityVo implements VoConvertable<ActivityVo, Activity> {

    private Integer id; //活动ID
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId; //发布者用户id
    private String userType; //发布账户类型(个人账户:1/企业账户:2/管理账户:100)
    private String title; //活动标题
    private String poster; //活动海报
    private String description; //活动详情
    private Byte form; //活动形式, 线上0, 线下1
    private CityVo city; //活动城市
    private Long activityStartTime; //活动开始时间
    private Long activityFinishTime; //活动结束时间
    private Long registrationStartTime; //报名开始时间
    private Long registrationFinishTime; //报名结束时间
    private String needResume; //参与活动是否需要上传简历
    private String onlyOverseasStudent;//仅留学生能参加
    private String auditType;//报名审核类型(是否需要审核) 0需要;1不需要
    private Integer numberLimit;//报名人数限制
    private String address; //活动地址, 在线活动则为url
    private Byte status; //活动状态, 0:审核中 1:草稿,2:下架,3:即将开始,4:报名中,5:进行中,6已结束;7:审核失败
    private Integer follower;//关注人数
    private Integer applicantQuantity;//报名人数
    private Integer weight;//排名权重
    private String sendNoticeConfirm;//报名审核后是否需要通知,0不再提示,1需要提示
    private Boolean joinFlag; //是否参加
    private Long joinTime; //参加时间
    private Long curTime; //系统服务器当前时间, 前端计算剩余多久用

    private String organizerType; //举办方类型
    private OrganizerInfoVo organizerInfoVo; //举办方信息vo
    private Integer registrationTemplateId; //问卷id


    @Override
    public ActivityVo asVo(Activity activity) {
        if (activity == null) return null;

        BeanUtil.copyProperties(activity, this, "activityStartTime", "activityFinishTime", "registrationStartTime", "registrationFinishTime", "joinTime", "curTime");

        setPoster(AppUtils.absOssUrl(activity.getPoster()));
        setCurTime(System.currentTimeMillis());

        setRegistrationStartTime(activity.getRegistrationStartTime().getTime());
        setRegistrationFinishTime(activity.getRegistrationFinishTime().getTime());
        setActivityStartTime(activity.getActivityStartTime().getTime());
        setActivityFinishTime(activity.getActivityFinishTime().getTime());

        return this;
    }
}
