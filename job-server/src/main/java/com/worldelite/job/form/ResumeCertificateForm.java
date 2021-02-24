package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Nuo Xu
 */
@Data
public class ResumeCertificateForm {
    public Integer getId() {
        return id;
    }

    private Integer id;
    private Long resumeId;
    @NotBlank
    private String title; //标题
    @JSONField(format = "yyyy-MM-dd")
    private Date time;

}