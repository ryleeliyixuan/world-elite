package com.worldelite.job.form;

import lombok.Data;

/**
 * 企业组织架构表单
 */
@Data
public class CompanyStructureForm {
    private Integer id; //组织架构ID
    private Integer parentId; //父级ID
    private Long companyId; //公司ID
    private String name; //组织架构名
}
