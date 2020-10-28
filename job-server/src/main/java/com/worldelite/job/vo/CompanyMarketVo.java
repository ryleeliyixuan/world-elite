package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanyMarket;
import lombok.Data;

/**
 * 企业市值视图对象
 */
@Data
public class CompanyMarketVo implements VoConvertable<CompanyMarketVo, CompanyMarket>{

    private Integer id; //发展路径ID
    private String companyId; //公司ID
    private String url; //市值接口URL

    @Override
    public CompanyMarketVo asVo(CompanyMarket companyMarket) {
        if(companyMarket==null) return null;
        setId(companyMarket.getId());
        setCompanyId(String.valueOf(companyMarket.getCompanyId()));
        setUrl(companyMarket.getUrl());
        return this;
    }
}
