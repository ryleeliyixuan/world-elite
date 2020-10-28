package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyMarket;
import com.worldelite.job.form.CompanyMarketForm;
import com.worldelite.job.mapper.CompanyMarketMapper;
import com.worldelite.job.vo.CompanyMarketVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业市值服务类
 */
@Slf4j
@Service
public class CompanyMarketService extends BaseService{

    @Autowired
    private CompanyMarketMapper companyMarketMapper;

    /**
     * 保存市值
     * @param companyMarketForm 市值表单
     */
    public CompanyMarket save(CompanyMarketForm companyMarketForm){
        CompanyMarket companyMarket = new CompanyMarket();
        BeanUtil.copyProperties(companyMarketForm,companyMarket);
        //公司市值URL只能有一个，所以需要先删除旧URL
        companyMarketMapper.deleteByCompanyId(companyMarketForm.getCompanyId());
        companyMarketMapper.insertSelective(companyMarket);
        return companyMarket;
    }

    /**
     * 通过ID查询市值
     * @param id 市值ID
     * @return 市值
     */
    public CompanyMarket getById(Integer id){
        return companyMarketMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取公司市值
     * @param companyId 公司ID
     * @return
     */
    public CompanyMarket getByCompanyId(Long companyId){
        CompanyMarket companyMarket = new CompanyMarket();
        companyMarket.setCompanyId(companyId);
        List<CompanyMarket> marketList = companyMarketMapper.selectAndList(companyMarket);
        if(CollectionUtils.isNotEmpty(marketList)){
            return marketList.get(0);
        }else{
            return null;
        }
    }

    public CompanyMarketVo toVo(CompanyMarket companyMarket){
        return new CompanyMarketVo().asVo(companyMarket);
    }

    /**
     * 根据ID删除市值
     * @param id 市值ID
     */
    public void deleteById(Integer id){
        companyMarketMapper.deleteByPrimaryKey(id);
    }

}
