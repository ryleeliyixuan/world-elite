package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 简历分享表单
 */
@Data
public class ResumeShareForm {
    @NotNull(message = "{api.error.invalid.param}")
    private Long resumeId; //简历ID
    @NotNull(message = "{api.error.invalid.param}")
    private Long toId; //目标公司ID
}
