package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业帖子搜索表单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyPostListForm extends PageForm{
    private Long id; //帖子ID
    private Long companyId; //企业ID
    private Integer cliqueId; //圈子ID
    private String title; //帖子标题
}
