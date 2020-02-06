package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeEduForm {
    private Integer id;
    private Long resumeId;
    private String schoolName;
    private String majorName;
    private Integer degreeId;
    @JSONField(format = "yyyy-MM")
    private Date startTime;
    @JSONField(format = "yyyy-MM")
    private Date finishTime;
    private Double gpa;
}
