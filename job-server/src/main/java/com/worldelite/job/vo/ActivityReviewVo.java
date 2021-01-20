package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.worldelite.job.entity.ActivityReview;
import lombok.Data;

import java.util.Date;

/**
 * @author Xiang Chao
 **/
@Data
public class ActivityReviewVo implements VoConvertable<ActivityReviewVo, ActivityReview> {
    private Integer id; //审核id
    private Integer activityId; //活动id
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId; //发布活动的用户id/企业id
    private String status; //状态.1:审核中,2:通过,3拒绝
    private String reason; //审核失败原因,若状态为拒绝时
    private Date createTime;
    private Date updateTime;

    @Override
    public ActivityReviewVo asVo(ActivityReview activityReview) {
        if (activityReview == null) return null;
        BeanUtil.copyProperties(activityReview, this);

        return this;
    }
}
