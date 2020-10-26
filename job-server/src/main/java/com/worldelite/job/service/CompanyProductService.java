package com.worldelite.job.service;

import com.worldelite.job.entity.CompanyProduct;
import com.worldelite.job.mapper.CompanyProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业产品服务类
 */
@Slf4j
@Service
public class CompanyProductService extends BaseService{

    @Autowired
    private CompanyProductMapper companyProductMapper;

    /**
     * 保存产品
     * @param companyProduct 产品表单
     */
    public void save(CompanyProduct companyProduct){
        if(companyProduct.getId() == null){
            companyProductMapper.insertSelective(companyProduct);
            return;
        }
        companyProductMapper.updateByPrimaryKeySelective(companyProduct);
    }

    /**
     * 通过ID查询产品
     * @param id 产品ID
     * @return 产品
     */
    public CompanyProduct getById(Integer id){
        return companyProductMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询企业对应的所有产品
     * @param companyId 企业ID
     * @return 产品列表
     */
    public List<CompanyProduct> getByCompanyId(Long companyId){
        return null;
    }

    /**
     * 根据ID删除产品
     * @param id 产品ID
     */
    public void deleteById(Integer id){
        companyProductMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除企业所有产品
     * @param companyId 企业ID
     */
    public void deleteByCompanyId(Long companyId){
        ;
    }

}
