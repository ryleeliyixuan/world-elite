package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 简历下载查询表单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResumeDownloadListForm extends PageForm{
    private Integer id; //简历下载ID
    private Long resumeId; //简历ID
    private String name; //简历下载名
    private Byte status; //简历下载状态
}
