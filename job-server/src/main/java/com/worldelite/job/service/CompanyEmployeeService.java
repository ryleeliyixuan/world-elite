package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyEmployee;
import com.worldelite.job.entity.CompanyReport;
import com.worldelite.job.form.CompanyReportForm;
import com.worldelite.job.mapper.CompanyEmployeeMapper;
import com.worldelite.job.mapper.CompanyReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业雇员数量服务类
 */
@Slf4j
@Service
public class CompanyEmployeeService extends BaseService{

    @Autowired
    private CompanyEmployeeMapper companyEmployeeMapper;

    /**
     * 保存雇员数量
     * @param companyEmployee 雇员数量表单
     */
    public void save(CompanyEmployee companyEmployee){
        if(companyEmployee.getId() == null){
            companyEmployeeMapper.insertSelective(companyEmployee);
            return;
        }
        companyEmployeeMapper.updateByPrimaryKeySelective(companyEmployee);
    }

    /**
     * 通过ID查询雇员数量
     * @param id 雇员数量ID
     * @return 雇员数量
     */
    public CompanyEmployee getById(Integer id){
        return companyEmployeeMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询企业对应的所有雇员数量
     * @param companyId 企业ID
     * @return 雇员数量列表
     */
    public List<CompanyEmployee> getByCompanyId(Long companyId){
        return null;
    }

    /**
     * 根据ID删除雇员数量
     * @param id 雇员数量ID
     */
    public void deleteById(Integer id){
        companyEmployeeMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除企业所有雇员数量
     * @param companyId 企业ID
     */
    public void deleteByCompanyId(Long companyId){
        ;
    }

}
