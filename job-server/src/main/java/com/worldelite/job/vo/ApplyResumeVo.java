package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 *
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ApplyResumeVo {
    private Integer id;
    private ResumeVo resume; //简历
    private JobVo job; //职位
    private List<UserCorporateCommentVo> commentVos;
    private List<UserCorporateTagVo> tagVos;
    private Byte applyStatus; //状态
    @JSONField(format = "yyyy-MM-dd")
    private Date time; //申请时间
    private Byte type; //简历类型：1.用户,2.企业
}
