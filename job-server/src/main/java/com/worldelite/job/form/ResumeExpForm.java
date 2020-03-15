package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeExpForm {
    private Integer id;
    private Long resumeId;
    @NotBlank(message = "{edit.resume.exp.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date startTime;
    @NotBlank(message = "{edit.resume.exp.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date finishTime;
    @NotBlank(message = "{edit.resume.exp.company.not.blank}")
    private String company;
    @NotBlank(message = "{edit.resume.exp.depart.not.blank}")
    private String depart;
    @NotBlank(message = "{edit.resume.exp.post.not.blank}")
    private String post;
    @NotBlank(message = "{edit.resume.exp.description.not.blank}")
    private String description;
}
