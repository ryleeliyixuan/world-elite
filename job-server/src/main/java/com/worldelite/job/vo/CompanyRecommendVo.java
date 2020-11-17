package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanyRecommend;
import lombok.Data;

@Data
public class CompanyRecommendVo implements VoConvertable<CompanyRecommendVo, CompanyRecommend>{

    private Integer id; //ID
    private String companyId; //公司ID
    private String objectId; //对象ID
    private Byte objectType; //对象类型
    private Integer position; //排序

    @Override
    public CompanyRecommendVo asVo(CompanyRecommend companyRecommend) {
        if(companyRecommend==null) return null;
        setId(companyRecommend.getId());
        setCompanyId(String.valueOf(companyRecommend.getCompanyId()));
        setObjectId(String.valueOf(companyRecommend.getObjectId()));
        setObjectType(companyRecommend.getObjectType());
        setPosition(companyRecommend.getPosition());
        return this;
    }
}
