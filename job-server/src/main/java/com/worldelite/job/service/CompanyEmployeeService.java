package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyEmployee;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyEmployeeForm;
import com.worldelite.job.mapper.CompanyEmployeeMapper;
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
     * @param companyEmployeeForm 雇员数量表单
     */
    public void save(CompanyEmployeeForm companyEmployeeForm){
        Long companyId = companyEmployeeForm.getCompanyId();
        //删除旧数据
        deleteByCompanyId(companyId);
        //插入新数据
        CompanyEmployee companyEmployee = new CompanyEmployee();
        BeanUtil.copyProperties(companyEmployeeForm,companyEmployee);
        companyEmployeeMapper.insertSelective(companyEmployee);
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
     * 删除企业所有雇员数量
     * @param companyId 企业ID
     */
    public void deleteByCompanyId(Long companyId){
        companyEmployeeMapper.deleteByCompanyId(companyId);
    }

}
