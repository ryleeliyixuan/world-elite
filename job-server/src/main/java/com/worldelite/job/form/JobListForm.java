package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobListForm extends PageForm{
    private Long creatorId;
    private Long companyId;
    private Integer jobType;
    private String name;
    private Integer cityId;
    private Byte status;
}
