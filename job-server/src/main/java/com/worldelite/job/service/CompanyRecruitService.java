package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyRecruit;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyRecruitForm;
import com.worldelite.job.mapper.CompanyRecruitMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyRecruitVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyRecruitService extends BaseService{
	
	@Autowired
	private CompanyRecruitMapper companyRecruitMapper;

	public CompanyRecruit save(CompanyRecruitForm companyRecruitForm){
		Integer id = companyRecruitForm.getId();
		CompanyRecruit companyRecruit = new CompanyRecruit();
		BeanUtil.copyProperties(companyRecruitForm,companyRecruit);
		if(id == null){
			companyRecruitMapper.insertSelective(companyRecruit);
		}else{
			companyRecruitMapper.updateByPrimaryKeySelective(companyRecruit);
		}
		return companyRecruit;
	}

	public void deleteById(Integer id){
		companyRecruitMapper.deleteByPrimaryKey(id);
	}

	public List<CompanyRecruit> listByCompanyId(Long companyId){
		CompanyRecruit companyRecruit = new CompanyRecruit();
		companyRecruit.setCompanyId(companyId);
		return companyRecruitMapper.selectAndList(companyRecruit);
	}

	public CompanyRecruitVo toVo(CompanyRecruit companyRecruit){
		return new CompanyRecruitVo().asVo(companyRecruit);
	}

	public List<CompanyRecruitVo> listVoByCompanyId(Long companyId){
		List<CompanyRecruit> companyRecruitList = listByCompanyId(companyId);
		return AppUtils.asVoList(companyRecruitList,CompanyRecruitVo.class);
	}

}
