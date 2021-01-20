package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Xiang Chao
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class ActivityReviewForm extends PageForm {
    private Integer id; //审核id
    private Integer activityId; //活动id
    private Long userId; //发布活动的用户id/企业id
    private String status; //状态.1:审核中,2:通过,3拒绝
    private String reason; //审核失败原因,若状态为拒绝时
}
