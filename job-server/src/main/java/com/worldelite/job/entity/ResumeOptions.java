package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */

/**
 * Nuo Xu
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
    private Long creatorId;
    private String extQuery; //额外查询条件

    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


}
