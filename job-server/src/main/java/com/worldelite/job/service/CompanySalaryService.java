package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanySalary;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanySalaryForm;
import com.worldelite.job.mapper.CompanySalaryMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanySalaryVo;
import com.worldelite.job.vo.DictVo;
import com.worldelite.job.vo.JobVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CompanySalaryService extends BaseService{
	
	@Autowired
	private CompanySalaryMapper companySalaryMapper;

	@Autowired
	private JobService jobService;

	public CompanySalary save(CompanySalaryForm companySalaryForm){
		Integer id = companySalaryForm.getId();
		CompanySalary companySalary = new CompanySalary();
		BeanUtil.copyProperties(companySalaryForm,companySalary);
		if(id == null){
			companySalaryMapper.insertSelective(companySalary);
		}else{
			companySalaryMapper.updateByPrimaryKeySelective(companySalary);
		}
		return companySalary;
	}

	public void deleteById(Integer id){
		companySalaryMapper.deleteByPrimaryKey(id);
	}

	public List<CompanySalary> listByCompanyId(Long companyId){
		CompanySalary companySalary = new CompanySalary();
		companySalary.setCompanyId(companyId);
		return companySalaryMapper.selectAndList(companySalary);
	}

	public CompanySalaryVo toVo(CompanySalary companySalary){
		CompanySalaryVo companySalaryVo = new CompanySalaryVo().asVo(companySalary);
		if(companySalary.getJobId() != null){
			JobVo job = jobService.getJobInfo(companySalary.getJobId(),false);
			companySalaryVo.setJob(job);
		}
		return companySalaryVo;
	}

	public List<CompanySalaryVo> listVoByCompanyId(Long companyId){
		List<CompanySalary> companySalaryList = listByCompanyId(companyId);
		List<CompanySalaryVo> companySalaryVoList = new ArrayList<>(companySalaryList.size());
		for(CompanySalary companySalary:companySalaryList){
			companySalaryVoList.add(toVo(companySalary));
		}
		return companySalaryVoList;
	}

}
