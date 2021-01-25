package com.worldelite.job.form;

import lombok.Data;

import javax.print.DocFlavor;
import javax.validation.constraints.NotEmpty;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobForm {
    private String expectCity; //意向城市

    public Integer getCategoryId() {
        return categoryId;
    }

    private Integer categoryId; //意向职位

    public Integer getSalaryId() {
        return salaryId;
    }

    private Integer salaryId;

    public Long getResumeId() {
        return resumeId;
    }

    private Long resumeId;

    public String getExpectCity() {
        return expectCity;
    }

    public String getExpectWorkType() {
        return expectWorkType;
    }

    public String getExpectPosition() {
        return expectPosition;
    }

    private String expectWorkType; // 工作类型
    private String expectPosition; //expect position

    public String getIndustry() {
        return industry;
    }

    private String industry;
}
