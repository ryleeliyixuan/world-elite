package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Nuo Xu
 */
@Data
public class ResumeLanguageForm {
    public Integer getId() {
        return id;
    }

    private Integer id;
    private Long resumeId;
    @NotBlank
    private String title; //标题
    @NotBlank
    private String description; //描述

}