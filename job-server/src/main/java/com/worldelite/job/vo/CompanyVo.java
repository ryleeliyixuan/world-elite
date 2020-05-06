package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.entity.Company;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

import java.util.Date;
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

    private String synopsis;

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

    private String wikiSummary;
    private String companyWiki;

    private Byte favoriteFlag = Bool.FALSE; //是否已收藏
    @JSONField(format = "yyyy-MM-dd")
    private Date favoriteTime; //收藏时间

    @Override
    public CompanyVo asVo(Company company) {
        setId(String.valueOf(company.getId()));
        setName(company.getName());
        setFullName(company.getFullName());
        setLogo(AppUtils.absOssUrl(company.getLogo()));
        setSynopsis(company.getSynopsis());
        setHomepage(company.getHomepage());
        setIntroduction(company.getIntroduction());
        return this;
    }
}
