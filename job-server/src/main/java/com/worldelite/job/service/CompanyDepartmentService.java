package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.CompanyDepartment;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyDepartmentForm;
import com.worldelite.job.mapper.CompanyDepartmentMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyDepartmentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CompanyDepartmentService extends BaseService{
	
	@Autowired
	private CompanyDepartmentMapper companyDepartmentMapper;

	public CompanyDepartment save(CompanyDepartmentForm companyDepartmentForm){
		Integer id = companyDepartmentForm.getId();
		CompanyDepartment companyDepartment = new CompanyDepartment();
		BeanUtil.copyProperties(companyDepartmentForm,companyDepartment);
		if(id == null){
			companyDepartmentMapper.insertSelective(companyDepartment);
		}else{
			companyDepartmentMapper.updateByPrimaryKeySelective(companyDepartment);
		}
		return companyDepartment;
	}

	public void deleteById(Integer id){
		companyDepartmentMapper.deleteByPrimaryKey(id);
	}

	public List<CompanyDepartment> listByCompanyId(Long companyId){
		CompanyDepartment companyDepartment = new CompanyDepartment();
		companyDepartment.setCompanyId(companyId);
		return companyDepartmentMapper.selectAndList(companyDepartment);
	}

	public CompanyDepartmentVo toVo(CompanyDepartment companyDepartment){
		return new CompanyDepartmentVo().asVo(companyDepartment);
	}

	public List<CompanyDepartmentVo> listVoByCompanyId(Long companyId){
		List<CompanyDepartment> companyDepartmentList = listByCompanyId(companyId);
		Map<Integer,CompanyDepartmentVo> nodes = new HashMap<>();
		for(CompanyDepartment companyDepartment:companyDepartmentList){
			nodes.put(companyDepartment.getId(),new CompanyDepartmentVo().asVo(companyDepartment));
		}
		List<CompanyDepartmentVo> roots = new ArrayList<>();
		for(Integer id:nodes.keySet()){
			CompanyDepartmentVo department = nodes.get(id);
			if(department.getParentId()==null || department.getParentId()==0 ||
					nodes.get(department.getParentId())==null){
				roots.add(department);
			}else{
				CompanyDepartmentVo parent = nodes.get(department.getParentId());
				if(CollectionUtils.isEmpty(parent.getChildren())){
					parent.setChildren(new ArrayList<>());
				}
				parent.getChildren().add(department);
			}
		}
		return roots;
	}

}
