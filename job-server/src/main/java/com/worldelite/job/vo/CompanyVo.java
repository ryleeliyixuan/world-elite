package com.worldelite.job.vo;

import com.worldelite.job.entity.Company;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyVo implements VoConvertable<CompanyVo, Company>{

    private Long id;
    private String name;
    private String fullName;

    @Override
    public CompanyVo asVo(Company company) {
        setId(company.getId());
        setName(company.getName());
        setFullName(company.getFullName());
        return this;
    }
}
