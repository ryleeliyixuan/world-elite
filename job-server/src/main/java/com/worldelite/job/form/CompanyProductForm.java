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
    private String imageName; //图片名
    private String imageUrl; //图片路径
    private String url; //产品链接
    private Integer position; //排序
}
