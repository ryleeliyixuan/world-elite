package com.worldelite.job.form;

import lombok.Data;

@Data
public class CompanyRecommendForm {
	private Integer id;
    private Long companyId;
    private Long objectId; //推荐对象ID
    private Byte objectType; //推荐对象类型
    private Integer position; //推荐排序
}
