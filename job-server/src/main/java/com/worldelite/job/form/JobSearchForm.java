package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobSearchForm extends PageForm{
    private String keyword;
    private Integer salaryRangeId;
    private Integer minSalary;
    private Integer maxSalary;
    private Integer jobType;
    private Integer[] cityIds;
    private Integer[] companyIndustryIds;
    private Integer[] companyScaleIds;
    private Integer[] companyStageIds;
    private Integer[] degreeIds;
    private Integer[] categoryIds;
}
