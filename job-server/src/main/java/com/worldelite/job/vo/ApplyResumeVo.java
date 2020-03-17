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
    private ResumeVo resume;
    private JobVo job;
    private Byte applyStatus;
    @JSONField(format = "yyyy-MM-dd")
    private Date time;
}
