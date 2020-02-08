package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeExpForm {
    private Integer id;
    private Long resumeId;
    @JSONField(format = "yyyy-MM")
    private Date startDate;
    @JSONField(format = "yyyy-MM")
    private Date finishDate;
    private String company;
    private String depart;
    private String post;
    private String description;
}
