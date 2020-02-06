package com.worldelite.job.form;

import lombok.Data;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeExpForm {
    private Integer id;
    private Long resumeId;
    private Date startDate;
    private Date finishDate;
    private String company;
    private String position;
    private String description;
}
