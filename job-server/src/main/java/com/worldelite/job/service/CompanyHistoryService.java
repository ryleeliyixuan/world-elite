package com.worldelite.job.service;

import com.worldelite.job.entity.CompanyHistory;
import com.worldelite.job.mapper.CompanyHistoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业发展路径服务类
 */
@Slf4j
@Service
public class CompanyHistoryService extends BaseService{

    @Autowired
    private CompanyHistoryMapper companyHistoryMapper;

    /**
     * 保存发展路径
     * @param companyHistory 发展路径表单
     */
    public void save(CompanyHistory companyHistory){
        if(companyHistory.getId() == null){
            companyHistoryMapper.insertSelective(companyHistory);
            return;
        }
        companyHistoryMapper.updateByPrimaryKeySelective(companyHistory);
    }

    /**
     * 通过ID查询发展路径
     * @param id 发展路径ID
     * @return 发展路径
     */
    public CompanyHistory getById(Integer id){
        return companyHistoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询企业对应的所有发展路径
     * @param companyId 企业ID
     * @return 发展路径列表
     */
    public List<CompanyHistory> getByCompanyId(Long companyId){
        return null;
    }

    /**
     * 根据ID删除发展路径
     * @param id 发展路径ID
     */
    public void deleteById(Integer id){
        companyHistoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除企业所有发展路径
     * @param companyId 企业ID
     */
    public void deleteByCompanyId(Long companyId){
        ;
    }

}
