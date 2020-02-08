package com.worldelite.job.form;

import lombok.Data;

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
