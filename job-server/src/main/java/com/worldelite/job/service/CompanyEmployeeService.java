package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyEmployee;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyEmployeeForm;
import com.worldelite.job.mapper.CompanyEmployeeMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyEmployeeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
    public CompanyEmployee save(CompanyEmployeeForm companyEmployeeForm){
        //保证一个年份只有一个雇员数量
        CompanyEmployee companyEmployee = new CompanyEmployee();
        BeanUtil.copyProperties(companyEmployeeForm,companyEmployee);
        List<CompanyEmployee> employeeList = companyEmployeeMapper.selectAndList(companyEmployee);
        if(CollectionUtils.isNotEmpty(employeeList)){
            throw new ServiceException(message("api.error.data.repeat"));
        }
        companyEmployeeMapper.insertSelective(companyEmployee);
        return companyEmployee;
    }

    public CompanyEmployeeVo toVo(CompanyEmployee companyEmployee){
        return new CompanyEmployeeVo().asVo(companyEmployee);
    }

    /**
     * 删除雇员数量
     * 删除雇员数量
     * @param employeeId
     */
    public void deleteById(Integer employeeId){
        companyEmployeeMapper.deleteByPrimaryKey(employeeId);
    }

    /**
     * 查询企业对应的所有雇员数量
     * @param companyId 企业ID
     * @return 雇员数量列表
     */
    public List<CompanyEmployee> getByCompanyId(Long companyId){
        CompanyEmployee companyEmployee = new CompanyEmployee();
        companyEmployee.setCompanyId(companyId);
        return companyEmployeeMapper.selectAndList(companyEmployee);
    }

    public List<CompanyEmployeeVo> listEmployeeVo(Long companyId){
        List<CompanyEmployee> employeeList = getByCompanyId(companyId);
        return AppUtils.asVoList(employeeList,CompanyEmployeeVo.class);
    }
}
