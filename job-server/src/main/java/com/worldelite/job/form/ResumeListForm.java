package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeListForm extends PageForm{
    private Long userId;
    private String name;
    private Integer[] degreeIds;
    private Integer[] schoolIds;
    private Double minGpa;
    private Double maxGpa;
}
