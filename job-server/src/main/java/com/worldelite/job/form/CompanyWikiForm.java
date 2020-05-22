package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyWikiForm {
    private Long companyId; //公司ID
    private String summary; //摘要
    private String content; //百科详情
}
