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
    private Integer id; //教育经历ID
    private Long resumeId; //简历ID
    private Long userId; //简历用户ID
    @NotBlank(message = "{edit.resume.edu.school.name.not.blank}")
    private String schoolName; //学校
    @NotBlank(message = "{edit.resume.edu.major.not.blank}")
    private String majorName; //专业
    @NotNull(message = "{edit.resume.edu.degree.not.null}")
    private Integer degreeId; //学历
    @NotNull(message = "{edit.resume.edu.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date startTime; //开始时间
    @NotNull(message = "{edit.resume.edu.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date finishTime; //结束时间
    private Double gpa; //gpa
}
