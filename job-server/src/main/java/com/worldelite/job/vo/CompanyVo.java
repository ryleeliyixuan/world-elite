package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.entity.Company;
import com.worldelite.job.util.AppUtils;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyVo implements VoConvertable<CompanyVo, Company>{

    private String id; //公司ID
    @ResumeScore
    private String name; //公司简称
    @ResumeScore
    private String fullName; //公司全称
    @ResumeScore
    private String logo; //logo
    @ResumeScore
    private String homepage; //主页
    @ResumeScore
    private String introduction; //公司介绍

    private String synopsis; //公司一句话介绍

    @ResumeScore
    private DictVo scale; //公司规模
    @ResumeScore
    private DictVo stage; //公司阶段
    @ResumeScore
    private DictVo industry; //所属行业
    @ResumeScore
    private DictVo property; //公司性质

    @ResumeScore
    private List<CompanyAddressVo> addressList; //公司地址

    @Ignore
    private List<JobVo> jobList;

    private int completeProgress; //完善进度

    private String wikiSummary; //百科摘要
    private String companyWiki; //公司百科

    private Byte favoriteFlag = Bool.FALSE; //收藏标志
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
