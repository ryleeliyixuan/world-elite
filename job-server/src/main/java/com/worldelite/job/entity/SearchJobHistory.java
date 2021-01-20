package com.worldelite.job.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 
 * 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchJobHistory implements Serializable {
    private Long id;

    private Long userId;

    private String keyWord;

    private Byte filterCount;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}