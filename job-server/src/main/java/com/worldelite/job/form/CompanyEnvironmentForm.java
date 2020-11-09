package com.worldelite.job.form;

import lombok.Data;

@Data
public class CompanyEnvironmentForm {
	private Integer id;
    private Long companyId;
    private String imageName; //环境图片名
    private String imageUrl; //环境图片地址
    private String name; //环境名
    private Integer position; //环境排序
}
