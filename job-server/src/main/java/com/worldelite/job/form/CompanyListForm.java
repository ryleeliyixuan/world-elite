package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyListForm extends PageForm{
    private String name; //公司简称
    private String fullName; //公司全称
    private Integer industryId; //行业ID
    private Integer cityId; //城市ID
}
