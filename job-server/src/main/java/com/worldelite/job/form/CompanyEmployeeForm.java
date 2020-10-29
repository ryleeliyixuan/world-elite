package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * 企业雇员数量表单
 */
@Data
public class CompanyEmployeeForm {
    private Long companyId; //公司ID
    @JSONField(format = "yyyy")
    private Date year; //年份
    private Integer number; //年份对应雇员数量
}
