package com.worldelite.job.entity;

import com.worldelite.job.util.TimeUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobOptions extends Job{
    private Integer[] cityIds;
    private Integer minSalary;
    private Integer maxSalary;
    private Integer[] categoryIds;

    private Date startPubTime;
    private Date endPubTime;
}
