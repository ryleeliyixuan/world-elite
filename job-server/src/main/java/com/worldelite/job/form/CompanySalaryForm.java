package com.worldelite.job.form;

import lombok.Data;

@Data
public class CompanySalaryForm {
	private Integer id;
    private Long companyId;
    private Integer categoryId; //职位ID
    private Integer percent; //百分比
}
