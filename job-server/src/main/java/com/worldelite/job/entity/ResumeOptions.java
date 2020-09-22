package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeOptions extends Resume{
    private Integer minAge;
    private Integer maxAge;
    private String degreeIds;
    private String schoolIds;
    private String categoryIds;
    private String cityIds;
    private Double minGpa;
    private Double maxGpa;
    private Integer minSalary;
    private Integer maxSalary;
    private Long companyId;
}
