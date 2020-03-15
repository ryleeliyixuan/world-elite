package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeEduForm {
    private Integer id;
    private Long resumeId;
    @NotBlank(message = "{edit.resume.edu.school.name.not.blank}")
    private String schoolName;
    @NotBlank(message = "{edit.resume.edu.major.not.blank}")
    private String majorName;
    @NotNull(message = "{edit.resume.edu.degree.not.null}")
    private Integer degreeId;
    @NotNull(message = "{edit.resume.edu.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date startTime;
    @NotNull(message = "{edit.resume.edu.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date finishTime;
    private Double gpa;
}
