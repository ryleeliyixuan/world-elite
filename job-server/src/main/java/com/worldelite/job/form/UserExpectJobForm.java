package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobForm {
    @NotEmpty(message = "{edit.resume.expect.job.cityIds.not.empty}")
    private Integer[] cityIds;
    @NotEmpty(message = "{edit.resume.expect.job.categoryIds.not.empty}")
    private Integer[] categoryIds;
    private Integer minSalary;
    private Integer maxSalary;
}
