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
    private Integer id;
    private Long resumeId;
    @NotNull(message = "{edit.resume.practice.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date startTime;
    @NotNull(message = "{edit.resume.practice.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date finishTime;
    @NotBlank(message = "{edit.resume.practice.title.not.blank}")
    private String title;
    @NotBlank(message = "{edit.resume.practice.description.not.blank}")
    private String description;
    private String post;
}