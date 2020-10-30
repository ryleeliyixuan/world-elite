package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HTMLFilter;
import com.worldelite.job.entity.CompanyMarket;
import com.worldelite.job.entity.CompanyWiki;
import com.worldelite.job.form.CompanyWikiForm;
import com.worldelite.job.form.CompanyWikiListForm;
import com.worldelite.job.mapper.CompanyMapper;
import com.worldelite.job.mapper.CompanyWikiMapper;
import com.worldelite.job.util.FormUtils;
import com.worldelite.job.vo.CityVo;
import com.worldelite.job.vo.CompanyVo;
import com.worldelite.job.vo.CompanyWikiVo;
import com.worldelite.job.vo.PageResult;
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
    private CityService cityService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private CompanyScoreService companyScoreService;

    /**
     * 保存公司百科
     *
     * @param companyWikiForm
     */
    public void saveCompanyWiki(CompanyWikiForm companyWikiForm) {
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
            companyWikiVo.setCompany(companyService.getCompanyInfo(companyId));
            CompanyMarket companyMarket = companyMarketService.getByCompanyId(companyId);
            companyWikiVo.setContent(companyWiki.getContent());
            companyWikiVo.setSummary(companyWiki.getSummary());
            companyWikiVo.setVideo(companyWiki.getVideo());
            companyWikiVo.setFavoriteCount(favoriteService.getWikiFavoriteCount(companyId));
            companyWikiVo.setScore(companyScoreService.getCompanyScore(companyId));
            if(companyWiki.getCityEnable()!=0) {
                Integer cityId = companyWiki.getCityId();
                companyWikiVo.setCity(cityService.getCityVo(cityId));
            }
            if(companyWiki.getEmployeeEnable()!=0){
                companyWikiVo.setEmployeeList(companyEmployeeService.listEmployeeVo(companyId));
            }
            if(companyWiki.getProductEnable()!=0){
                companyWikiVo.setProductList(companyProductService.listProductVo(companyId));
            }
            if(companyWiki.getMarketEnable()!=0){
                companyWikiVo.setMarket(companyMarketService.toVo(companyMarket));
            }
            if(companyWiki.getHistoryEnable()!=0){
                companyWikiVo.setHistoryList(companyHistoryService.listHistoryVo(companyId));
            }
            if(companyWiki.getStructureEnable()!=0){
                companyWikiVo.setStructure(companyStructureService.getStructureTree(companyId));
            }
            companyWikiVo.setCityEnable(companyWiki.getCityEnable());
            companyWikiVo.setEmployeeEnable(companyWiki.getEmployeeEnable());
            companyWikiVo.setProductEnable(companyWiki.getProductEnable());
            companyWikiVo.setMarketEnable(companyWiki.getMarketEnable());
            companyWikiVo.setHistoryEnable(companyWiki.getHistoryEnable());
            companyWikiVo.setStructureEnable(companyWiki.getStructureEnable());
            return companyWikiVo;
        }
        return null;
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

    /***
     * 更新模块启用状态
     * @param companyWikiForm
     */
    public CompanyWiki changeModuleEnable(CompanyWikiForm companyWikiForm){
        //检查百科有效性，更新模块状态必须百科存在
        CompanyWiki options = new CompanyWiki();
        options.setCompanyId(companyWikiForm.getCompanyId());
        List<CompanyWiki> wikiList = companyWikiMapper.selectAndList(options);
        CompanyWiki companyWiki;
        if (CollectionUtils.isNotEmpty(wikiList)) {
            companyWiki = wikiList.get(0);
        } else {
            throw new SecurityException(message("api.error.data.company"));
        }
        BeanUtil.copyProperties(companyWikiForm, companyWiki);
        //更新状态
        companyWikiMapper.updateByPrimaryKeySelective(companyWiki);
        return companyWiki;
    }
}
