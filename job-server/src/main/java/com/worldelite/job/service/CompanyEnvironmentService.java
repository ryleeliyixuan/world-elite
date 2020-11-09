package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyEnvironment;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyEnvironmentForm;
import com.worldelite.job.mapper.CompanyEnvironmentMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyEnvironmentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyEnvironmentService extends BaseService{
	
	@Autowired
	private CompanyEnvironmentMapper companyEnvironmentMapper;

	public CompanyEnvironment save(CompanyEnvironmentForm companyEnvironmentForm){
		Integer id = companyEnvironmentForm.getId();
		CompanyEnvironment companyEnvironment = new CompanyEnvironment();
		BeanUtil.copyProperties(companyEnvironmentForm,companyEnvironment);
		if(id == null){
			companyEnvironmentMapper.insertSelective(companyEnvironment);
		}else{
			companyEnvironmentMapper.updateByPrimaryKeySelective(companyEnvironment);
		}
		return companyEnvironment;
	}

	public void deleteById(Integer id){
		companyEnvironmentMapper.deleteByPrimaryKey(id);
	}

	public List<CompanyEnvironment> listByCompanyId(Long companyId){
		CompanyEnvironment companyEnvironment = new CompanyEnvironment();
		companyEnvironment.setCompanyId(companyId);
		return companyEnvironmentMapper.selectAndList(companyEnvironment);
	}

	public CompanyEnvironmentVo toVo(CompanyEnvironment companyEnvironment){
		return new CompanyEnvironmentVo().asVo(companyEnvironment);
	}

	public List<CompanyEnvironmentVo> listVoByCompanyId(Long companyId){
		List<CompanyEnvironment> companyEnvironmentList = listByCompanyId(companyId);
		return AppUtils.asVoList(companyEnvironmentList,CompanyEnvironmentVo.class);
	}

}
