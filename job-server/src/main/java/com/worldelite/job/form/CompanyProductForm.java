package com.worldelite.job.form;

import lombok.Data;

/**
 * 企业产品表单
 */
@Data
public class CompanyProductForm {
    private Integer id; //产品ID
    private Long companyId; //公司ID
    private String name; //产品名
    private String description; //产品描述
    private String picture; //产品图片
}
