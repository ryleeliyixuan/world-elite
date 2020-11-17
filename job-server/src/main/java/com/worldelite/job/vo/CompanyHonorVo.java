package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyHonor;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyHonorVo implements VoConvertable<CompanyHonorVo, CompanyHonor>{

    private Integer id; //ID
    private String companyId; //公司ID
    @JSONField(format = "yyyy")
    private Date year; //年份
    private String honor; //荣誉

    @Override
    public CompanyHonorVo asVo(CompanyHonor companyHonor) {
        if(companyHonor==null) return null;
        setId(companyHonor.getId());
        setCompanyId(String.valueOf(companyHonor.getCompanyId()));
        setYear(companyHonor.getYear());
        setHonor(companyHonor.getHonor());
        return this;
    }
}
