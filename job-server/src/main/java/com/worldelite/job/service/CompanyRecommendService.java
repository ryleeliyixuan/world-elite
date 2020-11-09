package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.constants.CompanyRecommendType;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.entity.CompanyRecommend;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyRecommendForm;
import com.worldelite.job.mapper.CompanyRecommendMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyPostVo;
import com.worldelite.job.vo.CompanyRecommendVo;
import com.worldelite.job.vo.JobVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CompanyRecommendService extends BaseService{
	
	@Autowired
	private CompanyRecommendMapper companyRecommendMapper;

	@Autowired
	private CompanyPostService companyPostService;

	@Autowired
	private JobService jobService;

	public CompanyRecommend save(CompanyRecommendForm companyRecommendForm){
		Integer id = companyRecommendForm.getId();
		CompanyRecommend companyRecommend = new CompanyRecommend();
		BeanUtil.copyProperties(companyRecommendForm,companyRecommend);
		if(id == null){
			companyRecommendMapper.insertSelective(companyRecommend);
		}else{
			companyRecommendMapper.updateByPrimaryKeySelective(companyRecommend);
		}
		return companyRecommend;
	}

	public void deleteById(Integer id){
		companyRecommendMapper.deleteByPrimaryKey(id);
	}

	public List<CompanyRecommend> listByCompanyId(Long companyId){
		CompanyRecommend companyRecommend = new CompanyRecommend();
		companyRecommend.setCompanyId(companyId);
		return companyRecommendMapper.selectAndList(companyRecommend);
	}

	public CompanyRecommendVo toVo(CompanyRecommend companyRecommend){
		return new CompanyRecommendVo().asVo(companyRecommend);
	}

	public List<CompanyRecommendVo> listVoByCompanyId(Long companyId){
		List<CompanyRecommend> companyRecommendList = listByCompanyId(companyId);
		return AppUtils.asVoList(companyRecommendList,CompanyRecommendVo.class);
	}

	public List<CompanyPostVo> listPostByCompanyId(Long companyId){
		CompanyRecommend companyRecommend = new CompanyRecommend();
		companyRecommend.setCompanyId(companyId);
		companyRecommend.setObjectType(CompanyRecommendType.POST.value);
		List<CompanyRecommend> companyRecommendList = companyRecommendMapper.selectAndList(companyRecommend);
		List<CompanyPostVo> companyPostVoList = new ArrayList<>();
		for(CompanyRecommend recommend:companyRecommendList){
			CompanyPostVo post = companyPostService.getPostVo(recommend.getObjectId());
			if(post != null) {
				companyPostVoList.add(post);
			}
		}
		return companyPostVoList;
	}

	public List<JobVo> listJobByCompanyId(Long companyId){
		CompanyRecommend companyRecommend = new CompanyRecommend();
		companyRecommend.setCompanyId(companyId);
		companyRecommend.setObjectType(CompanyRecommendType.JOB.value);
		List<CompanyRecommend> companyRecommendList = companyRecommendMapper.selectAndList(companyRecommend);
		List<JobVo> jobVoList = new ArrayList<>();
		for(CompanyRecommend recommend:companyRecommendList){
			JobVo job = jobService.getJobInfo(recommend.getObjectId(),false);
			if(job != null){
				jobVoList.add(job);
			}
		}
		return jobVoList;
	}
}
