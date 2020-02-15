package com.worldelite.job.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyUserVo extends UserVo{
    private CompanyVo company;
    private String depart;
    private String post;
}
