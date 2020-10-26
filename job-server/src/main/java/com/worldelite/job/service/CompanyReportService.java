package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyReport;
import com.worldelite.job.form.CompanyReportForm;
import com.worldelite.job.mapper.CompanyReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业举报服务类
 */
@Slf4j
@Service
public class CompanyReportService extends BaseService{

    @Autowired
    private CompanyReportMapper companyReportMapper;

    /**
     * 改变举报状态
     * @param companyReportForm 举报表单
     */
    public void report(CompanyReportForm companyReportForm){
        CompanyReport companyReport = null;
        if(companyReport.getId() != null){
            companyReport = getCompanyReport(companyReportForm.getOwnerId());
        }
        if(companyReport==null){
            newCompanyReport(companyReportForm);
        }else{
            BeanUtil.copyProperties(companyReportForm,companyReport,"id","fromId");
            companyReportMapper.updateByPrimaryKeySelective(companyReport);
        }
    }

    /**
     * 获取当前用户举报数据
     * @param ownerId 对象ID
     * @return 举报数据
     */
    private CompanyReport getCompanyReport(Long ownerId){
        CompanyReport report = new CompanyReport();
        report.setOwnerId(ownerId);
        report.setFromId(curUser().getId());
        return companyReportMapper.select(report);
    }

    /**
     * 举报
     * @param companyReportForm 举报表单
     */
    private void newCompanyReport(CompanyReportForm companyReportForm){
        CompanyReport report = new CompanyReport();
        report.setOwnerId(companyReportForm.getOwnerId());
        report.setFromId(curUser().getId());
        report.setOptionId(companyReportForm.getOptionId());
        report.setContent(companyReportForm.getContent());
        companyReportMapper.insertSelective(report);
    }

    /**
     * 删除对象下所有用户举报信息
     * @param ownerId 对象ID
     */
    private void deleteByOwnerId(Long ownerId){
        companyReportMapper.deleteByOwnerId(ownerId);
    }
}
