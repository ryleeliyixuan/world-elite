package com.worldelite.job.form;

import lombok.Data;

@Data
public class CompanySalaryForm {
	private Integer id;
    private Long companyId;
    private Long jobId; //岗位ID
    private String salary; //薪资范围
    private Float percent; //百分比
}
