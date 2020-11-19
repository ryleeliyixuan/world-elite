package com.worldelite.job.form;

import lombok.Data;

@Data
public class CompanySalaryForm {
	private Integer id;
    private Long companyId;
    private Long jobId; //岗位ID
    private Integer salaryId; //薪资范围ID
    private Integer percent; //百分比
}
