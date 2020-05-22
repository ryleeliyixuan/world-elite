package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;


/**
 *
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ApplyResumeVo {
    private Integer id;
    private ResumeVo resume; //简历
    private JobVo job; //职位
    private Byte applyStatus; //状态
    @JSONField(format = "yyyy-MM-dd")
    private Date time; //申请时间
}
