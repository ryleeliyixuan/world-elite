package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class JobForm {
    private Long id;
    private String name;
    private Integer categoryId;
    private String depart;
    private Integer minDegreeId;
    private Integer minSalary;
    private Integer maxSalary;
    private Integer salaryMonths;
    private Integer addressId;
    private Integer jobType;
    private String description;
}
