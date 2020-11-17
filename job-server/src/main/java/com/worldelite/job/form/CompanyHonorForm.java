package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyHonorForm {
	private Integer id;
    private Long companyId;
    @JSONField(format = "yyyy")
    private Date year; //年份
    private String honor; //荣誉
}
