package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 简历下载表单
 */
@Data
public class ResumeDownloadForm {
    @NotNull(message = "{api.error.invalid.param}")
    private Long resumeId; //简历ID
    private String name; //下载名
    private String fileName; //文件名
    private Byte status; //下载状态
    private Integer process; //下载进度
    private String message; //下载信息
}
