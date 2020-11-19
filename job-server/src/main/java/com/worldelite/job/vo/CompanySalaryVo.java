package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanySalary;
import lombok.Data;

@Data
public class CompanySalaryVo implements VoConvertable<CompanySalaryVo, CompanySalary>{

    private Integer id; //ID
    private String companyId; //公司ID
    private JobVo job; //岗位
    private DictVo salary; //薪资范围
    private Integer percent; //百分比

    @Override
    public CompanySalaryVo asVo(CompanySalary companySalary) {
        if(companySalary==null) return null;
        setId(companySalary.getId());
        setCompanyId(String.valueOf(companySalary.getCompanyId()));
        setPercent(companySalary.getPercent());
        return this;
    }
}
