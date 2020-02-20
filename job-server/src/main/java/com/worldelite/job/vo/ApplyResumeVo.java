package com.worldelite.job.vo;

import lombok.Data;

import java.util.Date;


/**
 *
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ApplyResumeVo {
    private ResumeVo resume;
    private JobVo job;
    private Byte applyStatus;
    private Date time;
}
