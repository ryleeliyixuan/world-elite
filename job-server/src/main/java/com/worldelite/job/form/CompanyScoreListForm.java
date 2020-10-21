package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业评分搜索表单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyScoreListForm extends PageForm{
    private Long id; //帖子ID
    private Long fromId; //发帖人ID
    private Long companyId; //企业ID
}
