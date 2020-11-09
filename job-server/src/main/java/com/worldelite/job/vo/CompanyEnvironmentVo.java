package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanyEnvironment;
import lombok.Data;

@Data
public class CompanyEnvironmentVo implements VoConvertable<CompanyEnvironmentVo, CompanyEnvironment>{

    private Integer id; //ID
    private String companyId; //公司ID
    private String imageName; //图片名
    private String imageUrl; //图片路径
    private String name; //环境名
    private Integer position; //排序

    @Override
    public CompanyEnvironmentVo asVo(CompanyEnvironment companyEnvironment) {
        if(companyEnvironment==null) return null;
        setId(companyEnvironment.getId());
        setCompanyId(String.valueOf(companyEnvironment.getCompanyId()));
        setImageName(companyEnvironment.getImageName());
        setImageUrl(companyEnvironment.getImageUrl());
        setName(companyEnvironment.getName());
        setPosition(companyEnvironment.getPosition());
        return this;
    }
}
