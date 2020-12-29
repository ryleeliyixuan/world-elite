package com.worldelite.job.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ActivityForm {
    private Integer id; //活动ID

    @NotBlank
    private String title; //活动标题
    @NotBlank
    private String poster; // 缩略图
    @NotNull
    private Integer cityId; //城市ID
    @NotBlank
    private String description; //活动详情
    @NotBlank
    private String address; //活动地址

    private Long userId; //用户id,留空为当前登录用户
    private String userType; //用户类型,个人账户:1/企业账户:2/管理账户:100. 留空为当前用户类型
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date activityStartTime; //活动开始时间
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date activityFinishTime; //活动结束时间
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date registrationStartTime; //报名开始时间
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date registrationFinishTime; //报名结束时间
    @NotNull
    private String needResume; //是否需要报名者简历信息, 0不需要,1需要
    @NotNull
    private String onlyOverseasStudent; //是否仅留学生能参加,0不限制,1仅海外
    @NotNull
    private String auditType; //报名审核类型(是否需要审核),0不需要,1需要
    private Integer numberLimit; //报名人数限制
    private Byte status; //活动状态, 0:审核中 1:草稿,2:下架,3:即将开始,4:报名中,5:进行中,6已结束. 管理端会用来修改活动状态,用户端用不到
    private Integer registrationTemplateId; //报名表模板ID
    private Integer follower; //关注人数
    private Integer applicantQuantity; //报名人数
    private Integer weight; //置顶权重
    private String notice; //通知提示,0不再提示,1需要提示

}
