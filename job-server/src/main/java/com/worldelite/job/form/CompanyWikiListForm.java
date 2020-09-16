package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyWikiListForm extends PageForm{
    private String keyword;//关键词
    private Integer industryId;//行业ID
}
