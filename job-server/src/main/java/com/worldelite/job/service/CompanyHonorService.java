package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyHonor;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyHonorForm;
import com.worldelite.job.mapper.CompanyHonorMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyHonorVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyHonorService extends BaseService{
	
	@Autowired
	private CompanyHonorMapper companyHonorMapper;

	public CompanyHonor save(CompanyHonorForm companyHonorForm){
		Integer id = companyHonorForm.getId();
		CompanyHonor companyHonor = new CompanyHonor();
		BeanUtil.copyProperties(companyHonorForm,companyHonor);
		if(id == null){
			companyHonorMapper.insertSelective(companyHonor);
		}else{
			companyHonorMapper.updateByPrimaryKeySelective(companyHonor);
		}
		return companyHonor;
	}

	public void deleteById(Integer id){
		companyHonorMapper.deleteByPrimaryKey(id);
	}

	public List<CompanyHonor> listByCompanyId(Long companyId){
		CompanyHonor companyHonor = new CompanyHonor();
		companyHonor.setCompanyId(companyId);
		return companyHonorMapper.selectAndList(companyHonor);
	}

	public CompanyHonorVo toVo(CompanyHonor companyHonor){
		return new CompanyHonorVo().asVo(companyHonor);
	}

	public List<CompanyHonorVo> listVoByCompanyId(Long companyId){
		List<CompanyHonor> companyHonorList = listByCompanyId(companyId);
		return AppUtils.asVoList(companyHonorList,CompanyHonorVo.class);
	}

}
