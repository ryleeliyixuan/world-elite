package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyWiki;
import com.worldelite.job.form.CompanyWikiForm;
import com.worldelite.job.mapper.CompanyWikiMapper;
import com.worldelite.job.vo.CompanyWikiVo;
import org.apache.commons.collections.CollectionUtils;
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
        List<CompanyWiki> wikiList = companyWikiMapper.selectAndList(options);
        if(CollectionUtils.isNotEmpty(wikiList)){
            CompanyWiki companyWiki = wikiList.get(0);
            companyWikiVo.setContent(companyWiki.getContent());
        }
        return companyWikiVo;
    }
}
