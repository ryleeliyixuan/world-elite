package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumePracticeForm {
    private Integer id; //实践ID
    private Long resumeId; //简历ID
    @JSONField(format = "yyyy-MM")
    private Date startTime; //开始时间
    @JSONField(format = "yyyy-MM")
    private Date finishTime; //结束时间
    @NotBlank(message = "{edit.resume.practice.title.not.blank}")
    private String title; //标题
    @NotBlank(message = "{edit.resume.practice.description.not.blank}")
    private String description; //描述
    private String post; //职位
    private Byte onWork; //实践中
}