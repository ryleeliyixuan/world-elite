package com.worldelite.job.vo;

import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.Company;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyVo implements VoConvertable<CompanyVo, Company>{

    private String id;
    @ResumeScore
    private String name;
    @ResumeScore
    private String fullName;
    @ResumeScore
    private String logo;
    @ResumeScore
    private String homepage;
    @ResumeScore
    private String introduction;

    @ResumeScore
    private DictVo scale;
    @ResumeScore
    private DictVo stage;
    @ResumeScore
    private DictVo industry;
    @ResumeScore
    private DictVo property;

    @ResumeScore
    private List<CompanyAddressVo> addressList;

    private List<JobVo> jobList;

    private int completeProgress;

    @Override
    public CompanyVo asVo(Company company) {
        setId(String.valueOf(company.getId()));
        setName(company.getName());
        setFullName(company.getFullName());
        setLogo(AppUtils.absOssUrl(company.getLogo()));
        setHomepage(company.getHomepage());
        setIntroduction(company.getIntroduction());
        return this;
    }
}
