package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyEmployee;
import lombok.Data;

import java.util.Date;

/**
 * 企业雇员数量视图对象
 */
@Data
public class CompanyEmployeeVo implements VoConvertable<CompanyEmployeeVo, CompanyEmployee>{

    private Integer id; //雇员数量ID
    private String companyId; //公司ID
    @JSONField(format = "yyyy")
    private Date year; //年份
    private Integer number; //雇员数量

    @Override
    public CompanyEmployeeVo asVo(CompanyEmployee companyEmployee) {
        if(companyEmployee==null) return null;
        setId(companyEmployee.getId());
        setCompanyId(String.valueOf(companyEmployee.getCompanyId()));
        setYear(companyEmployee.getYear());
        setNumber(companyEmployee.getNumber());
        return this;
    }
}
