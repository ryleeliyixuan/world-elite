package com.worldelite.job.form;

import lombok.Data;

@Data
public class CompanyDepartmentForm {
	private Integer id;
    private Long companyId;
    private Integer parentId;
    private String name; //部门名
}
