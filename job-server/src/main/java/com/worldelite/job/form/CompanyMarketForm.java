package com.worldelite.job.form;

import lombok.Data;

/**
 * 企业市值表单
 */
@Data
public class CompanyMarketForm {
    private Integer id; //市值ID
    private Long companyId; //公司ID
    private String url; //市值URL
}
