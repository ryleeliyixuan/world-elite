package com.worldelite.job.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author
 *
 */
@Data
@Builder
public class SearchJobHistoryInfo implements Serializable {
    private Long id;

    private Long historyId;

    private Integer cityId;

    private Integer salaryId;

    private Integer degreeId;

    private Integer industryId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}