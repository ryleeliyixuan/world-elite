package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HTMLFilter;
import com.worldelite.job.entity.CompanyWiki;
import com.worldelite.job.form.CompanyWikiForm;
import com.worldelite.job.form.CompanyWikiListForm;
import com.worldelite.job.mapper.CompanyMapper;
import com.worldelite.job.mapper.CompanyWikiMapper;
import com.worldelite.job.util.FormUtils;
import com.worldelite.job.vo.CompanyVo;
import com.worldelite.job.vo.CompanyWikiVo;
import com.worldelite.job.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class CompanyWikiService {

    @Autowired
    private CompanyWikiMapper companyWikiMapper;

    @Autowired
    private CompanyService companyService;

    /**
     * 保存公司百科
     *
     * @param companyWikiForm
     */
    public void saveCompanyWiki(CompanyWikiForm companyWikiForm){
        CompanyWiki options = new CompanyWiki();
        options.setCompanyId(companyWikiForm.getCompanyId());
        List<CompanyWiki> wikiList = companyWikiMapper.selectAndList(options);
        CompanyWiki companyWiki;
        if(CollectionUtils.isEmpty(wikiList)){
            companyWiki = new CompanyWiki();
        }else{
            companyWiki = wikiList.get(0);
        }
        BeanUtil.copyProperties(companyWikiForm, companyWiki);
        if(StringUtils.isEmpty(companyWikiForm.getSummary()) && StringUtils.isNotEmpty(companyWikiForm.getContent())){
            companyWiki.setSummary(StringUtils.substring(FormUtils.removeAllHtmlTag(companyWikiForm.getContent()), 0, 150));
        }
        if(companyWiki.getId() != null){
            companyWikiMapper.updateByPrimaryKeySelective(companyWiki);
        }else{
            companyWikiMapper.insertSelective(companyWiki);
        }
    }

    /**
     * 获取公司百科
     *
     * @param companyId
     * @return
     */
    public CompanyWikiVo getCompanyWiki(Long companyId){
        CompanyWikiVo companyWikiVo = new CompanyWikiVo();
        companyWikiVo.setCompany(companyService.getCompanyInfo(companyId));
        CompanyWiki options = new CompanyWiki();
        options.setCompanyId(companyId);
        CompanyWiki companyWiki = companyWikiMapper.selectByCompanyId(companyId);
        if(companyWiki != null){
            companyWikiVo.setContent(companyWiki.getContent());
            companyWikiVo.setSummary(companyWiki.getSummary());
        }
        return companyWikiVo;
    }

    /**
     * 获取公司百科摘要
     *
     * @param companyId
     * @return
     */
    public String getCompanyWikiSummary(Long companyId){
        CompanyWiki options = new CompanyWiki();
        options.setCompanyId(companyId);
        List<CompanyWiki> wikiList = companyWikiMapper.selectAndList(options);
        if(CollectionUtils.isNotEmpty(wikiList)){
            return wikiList.get(0).getSummary();
        }
        return null;
    }
}
