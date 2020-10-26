package com.worldelite.job.service;

import com.worldelite.job.entity.CompanyMarket;
import com.worldelite.job.mapper.CompanyMarketMapper;
import lombok.extern.slf4j.Slf4j;
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
     * @param companyMarket 市值表单
     */
    public void save(CompanyMarket companyMarket){
        if(companyMarket.getId() == null){
            companyMarketMapper.insertSelective(companyMarket);
            return;
        }
        companyMarketMapper.updateByPrimaryKeySelective(companyMarket);
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
     * 查询企业对应的所有市值
     * @param companyId 企业ID
     * @return 市值列表
     */
    public List<CompanyMarket> getByCompanyId(Long companyId){
        return null;
    }

    /**
     * 根据ID删除市值
     * @param id 市值ID
     */
    public void deleteById(Integer id){
        companyMarketMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除企业所有市值
     * @param companyId 企业ID
     */
    public void deleteByCompanyId(Long companyId){
        ;
    }

}
