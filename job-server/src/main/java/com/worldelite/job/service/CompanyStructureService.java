package com.worldelite.job.service;

import com.worldelite.job.entity.CompanyStructure;
import com.worldelite.job.mapper.CompanyStructureMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业组织架构服务类
 */
@Slf4j
@Service
public class CompanyStructureService extends BaseService{

    @Autowired
    private CompanyStructureMapper companyStructureMapper;

    /**
     * 保存组织架构
     * @param companyStructure 组织架构表单
     */
    public void save(CompanyStructure companyStructure){
        if(companyStructure.getId() == null){
            companyStructureMapper.insertSelective(companyStructure);
            return;
        }
        companyStructureMapper.updateByPrimaryKeySelective(companyStructure);
    }

    /**
     * 通过ID查询组织架构
     * @param id 组织架构ID
     * @return 组织架构
     */
    public CompanyStructure getById(Integer id){
        return companyStructureMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询企业对应的所有组织架构
     * @param companyId 企业ID
     * @return 组织架构列表
     */
    public List<CompanyStructure> getByCompanyId(Long companyId){
        return null;
    }

    /**
     * 根据ID删除组织架构
     * @param id 组织架构ID
     */
    public void deleteById(Integer id){
        companyStructureMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除企业所有组织架构
     * @param companyId 企业ID
     */
    public void deleteByCompanyId(Long companyId){
        ;
    }

}
