package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyHistory;
import com.worldelite.job.form.CompanyHistoryForm;
import com.worldelite.job.mapper.CompanyHistoryMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyHistoryVo;
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
     * @param companyHistoryForm 发展路径表单
     */
    public CompanyHistory save(CompanyHistoryForm companyHistoryForm){
        CompanyHistory companyHistory = new CompanyHistory();
        BeanUtil.copyProperties(companyHistoryForm,companyHistory);
        if(companyHistoryForm.getId() == null){
            companyHistoryMapper.insertSelective(companyHistory);
            return companyHistory;
        }
        companyHistoryMapper.updateByPrimaryKeySelective(companyHistory);
        return companyHistory;
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
        CompanyHistory companyHistory = new CompanyHistory();
        companyHistory.setCompanyId(companyId);
        return companyHistoryMapper.selectAndList(companyHistory);
    }

    /**
     * 返回视图对象列表
     * @param companyId
     * @return
     */
    public List<CompanyHistoryVo> listHistoryVo(Long companyId){
        List<CompanyHistory> historyList = getByCompanyId(companyId);
        return AppUtils.asVoList(historyList, CompanyHistoryVo.class);
    }

    public CompanyHistoryVo toVo(CompanyHistory companyHistory){
        return new CompanyHistoryVo().asVo(companyHistory);
    }

    /**
     * 根据ID删除发展路径
     * @param id 发展路径ID
     */
    public void deleteById(Integer id){
        companyHistoryMapper.deleteByPrimaryKey(id);
    }
}
