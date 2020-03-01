package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeOptions extends Resume{
    private Integer[] degreeIds;
    private Integer[] schoolIds;
    private Double minGpa;
    private Double maxGpa;
}
