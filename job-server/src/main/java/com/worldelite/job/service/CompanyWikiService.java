package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HTMLFilter;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyWikiForm;
import com.worldelite.job.form.CompanyWikiListForm;
import com.worldelite.job.mapper.CompanyMapper;
import com.worldelite.job.mapper.CompanyWikiMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.FormUtils;
import com.worldelite.job.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.bridge.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class CompanyWikiService extends BaseService{

    @Autowired
    private CompanyWikiMapper companyWikiMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private WikiModuleService wikiModuleService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private CompanyHistoryService companyHistoryService;

    @Autowired
    private CompanyMarketService companyMarketService;

    @Autowired
    private CompanyProductService companyProductService;

    @Autowired
    private CompanyStructureService companyStructureService;

    @Autowired
    private CompanySalaryService companySalaryService;

    @Autowired
    private CompanyRecruitService companyRecruitService;

    @Autowired
    private CompanyEnvironmentService companyEnvironmentService;

    @Autowired
    private CompanyDepartmentService companyDepartmentService;

    @Autowired
    private CompanyHonorService companyHonorService;

    @Autowired
    private CompanyRecommendService companyRecommendService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private CompanyScoreService companyScoreService;

    /**
     * 保存公司百科
     *
     * @param companyWikiForm
     */
    public CompanyWiki saveCompanyWiki(CompanyWikiForm companyWikiForm) {
        CompanyWiki options = new CompanyWiki();
        options.setCompanyId(companyWikiForm.getCompanyId());
        List<CompanyWiki> wikiList = companyWikiMapper.selectAndList(options);
        CompanyWiki companyWiki;
        if (CollectionUtils.isEmpty(wikiList)) {
            companyWiki = new CompanyWiki();
        } else {
            companyWiki = wikiList.get(0);
        }
        BeanUtil.copyProperties(companyWikiForm, companyWiki);
        if (StringUtils.isEmpty(companyWikiForm.getSummary()) && StringUtils.isNotEmpty(companyWikiForm.getContent())) {
            companyWiki.setSummary(StringUtils.substring(FormUtils.removeAllHtmlTag(companyWikiForm.getContent()), 0, 150));
        }
        if (companyWiki.getId() != null) {
            companyWikiMapper.updateByPrimaryKeySelective(companyWiki);
        } else {
            companyWikiMapper.insertSelective(companyWiki);
        }
        return companyWiki;
    }

    public PageResult<CompanyWikiVo> listSimple(CompanyWikiListForm listForm){
        AppUtils.setPage(listForm);
        CompanyOptions companyOptions = new CompanyOptions();
        companyOptions.setIndustryId(listForm.getIndustryId());
        companyOptions.setName(listForm.getKeyword());
        Page<Company> page = (Page<Company>) companyMapper.searchWiki(companyOptions);
        PageResult<CompanyWikiVo> pageResult = new PageResult<>(page);
        List<CompanyWikiVo> wikiList = new ArrayList<>();
        for(Company company:page){
            CompanyWikiVo companyWikiVo = new CompanyWikiVo();
            WikiModule wikiModule = wikiModuleService.getModuleByCompanyId(company.getId());
            if(wikiModule == null){
                wikiModule = wikiModuleService.newModule(company.getId());
            }
            companyWikiVo.setCompany(new CompanyVo().asVo(company));
            companyWikiVo.setWikiModule(new WikiModuleVo().asVo(wikiModule));
            wikiList.add(companyWikiVo);
        }
        pageResult.setList(wikiList);
        return pageResult;
    }

    /**
     * 获取公司百科
     *
     * @param companyId
     * @return
     */
    public CompanyWikiVo getCompanyWiki(Long companyId) {
        CompanyWiki options = new CompanyWiki();
        options.setCompanyId(companyId);
        CompanyWiki companyWiki = companyWikiMapper.selectByCompanyId(companyId);
        return toVo(companyWiki);
    }

    public CompanyWikiVo toVo(CompanyWiki companyWiki){
        CompanyWikiVo companyWikiVo = new CompanyWikiVo();
        if (companyWiki != null) {
            Long companyId = companyWiki.getCompanyId();
            WikiModule wikiModule = wikiModuleService.getModuleByCompanyId(companyId);
            if(wikiModule == null){
                wikiModule = wikiModuleService.newModule(companyId);
            }
            companyWikiVo.setWikiModule(new WikiModuleVo().asVo(wikiModule));
            //百科基础信息
            companyWikiVo.setCompany(companyService.getCompanyInfo(companyId));
            companyWikiVo.setContent(companyWiki.getContent());
            companyWikiVo.setSummary(companyWiki.getSummary());
            companyWikiVo.setVideo(companyWiki.getVideo());
            companyWikiVo.setBanner(companyWiki.getBanner());
            companyWikiVo.setScore(companyScoreService.getCompanyScore(companyId));
            //地址
            companyWikiVo.setAddress(companyWiki.getAddress());
            companyWikiVo.setEmployeeList(companyEmployeeService.listEmployeeVo(companyId));
            companyWikiVo.setProductList(companyProductService.listProductVo(companyId));
            CompanyMarket companyMarket = companyMarketService.getByCompanyId(companyId);
            companyWikiVo.setMarket(companyMarketService.toVo(companyMarket));
            companyWikiVo.setHistoryList(companyHistoryService.listHistoryVo(companyId));
            companyWikiVo.setStructure(companyStructureService.getStructureTree(companyId));
            companyWikiVo.setSalaryList(companySalaryService.listVoByCompanyId(companyId));
            companyWikiVo.setRecruitList(companyRecruitService.listVoByCompanyId(companyId));
            companyWikiVo.setEnvironmentList(companyEnvironmentService.listVoByCompanyId(companyId));
            companyWikiVo.setDepartment(companyDepartmentService.listVoByCompanyId(companyId));
            companyWikiVo.setHonorList(companyHonorService.listVoByCompanyId(companyId));
            companyWikiVo.setPostList(companyRecommendService.listPostByCompanyId(companyId));
            companyWikiVo.setJobList(companyRecommendService.listJobByCompanyId(companyId));
            companyWikiVo.setFavoriteCount(favoriteService.getWikiFavoriteCount(companyId));
        }
        return companyWikiVo;
    }

    /**
     * 获取公司百科摘要
     *
     * @param companyId
     * @return
     */
    public String getCompanyWikiSummary(Long companyId) {
        CompanyWiki options = new CompanyWiki();
        options.setCompanyId(companyId);
        List<CompanyWiki> wikiList = companyWikiMapper.selectAndList(options);
        if (CollectionUtils.isNotEmpty(wikiList)) {
            return wikiList.get(0).getSummary();
        }
        return null;
    }
}
