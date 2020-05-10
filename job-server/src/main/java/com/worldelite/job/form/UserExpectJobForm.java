package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobForm {
    private Integer[] cityIds;
    private Integer[] categoryIds;
    private Integer minSalary;
    private Integer maxSalary;
}
