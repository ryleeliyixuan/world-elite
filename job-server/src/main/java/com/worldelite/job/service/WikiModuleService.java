package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.entity.WikiModule;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.WikiModuleForm;
import com.worldelite.job.mapper.WikiModuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WikiModuleService extends BaseService{

    @Autowired
    private WikiModuleMapper wikiModuleMapper;

    /**
     * 保存或转换模块启用状态
     * @param wikiModuleForm
     * @return
     */
    public WikiModule save(WikiModuleForm wikiModuleForm){
        WikiModule wikiModule = new WikiModule();
        BeanUtil.copyProperties(wikiModuleForm,wikiModule);
        //判断companyId是否存在记录
        List<WikiModule> wikiModuleList = wikiModuleMapper.selectAndList(wikiModule);
        if(CollectionUtils.isNotEmpty(wikiModuleList)){
            wikiModule.setId(wikiModuleList.get(0).getId());
        }
        if(wikiModule.getId()==null){
            wikiModuleMapper.insertSelective(wikiModule);
            return wikiModule;
        }
        wikiModuleMapper.updateByPrimaryKeySelective(wikiModule);
        return getModuleById(wikiModule.getId());
    }

    public WikiModule newModule(Long companyId){
        WikiModule wikiModule = new WikiModule();
        wikiModule.setCompanyId(companyId);
        wikiModule.setWikiEnable(Bool.TRUE);
        wikiModule.setAddressEnable(Bool.TRUE);
        wikiModule.setEmployeeEnable(Bool.TRUE);
        wikiModule.setProductEnable(Bool.TRUE);
        wikiModule.setMarketEnable(Bool.TRUE);
        wikiModule.setHistoryEnable(Bool.TRUE);
        wikiModule.setStructureEnable(Bool.TRUE);
        wikiModule.setSalaryEnable(Bool.TRUE);
        wikiModule.setRecruitEnable(Bool.TRUE);
        wikiModule.setEnvironmentEnable(Bool.TRUE);
        wikiModule.setDepartmentEnable(Bool.TRUE);
        wikiModule.setHonorEnable(Bool.TRUE);
        wikiModule.setPostEnable(Bool.TRUE);
        wikiModule.setJobEnable(Bool.TRUE);
        wikiModule.setCountEnable(Bool.TRUE);
        wikiModuleMapper.insertSelective(wikiModule);
        return wikiModule;
    }

    public WikiModule getModuleById(Integer id){
        return wikiModuleMapper.selectByPrimaryKey(id);
    }

    public WikiModule getModuleByCompanyId(Long companyId){
        WikiModule wikiModule = new WikiModule();
        wikiModule.setCompanyId(companyId);
        List<WikiModule> wikiModuleList = wikiModuleMapper.selectAndList(wikiModule);
        if(CollectionUtils.isEmpty(wikiModuleList)){
            return null;
        }
        return wikiModuleList.get(0);
    }
}
