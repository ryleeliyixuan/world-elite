package com.worldelite.job.vo;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyUserVo extends UserVo{
    private CompanyVo company; //公司
    private String depart; //部门
    private String post; //职位
}
