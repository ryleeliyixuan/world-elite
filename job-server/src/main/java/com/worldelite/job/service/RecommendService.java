package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.ObjectType;
import com.worldelite.job.entity.Recommend;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.form.RecommendForm;
import com.worldelite.job.form.RecommendListForm;
import com.worldelite.job.mapper.RecommendMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class RecommendService {

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private JobService jobService;

    @Autowired
    private CompanyService companyService;

    /**
     * 保存推荐
     */
    public void saveRecommend(RecommendForm recommendForm){
        if(recommendForm.getObjectType() == ObjectType.JOB.value){
            JobVo jobVo = jobService.getJobInfo(recommendForm.getObjectId(), false);
            if(jobVo == null){
                throw new ServiceException(ApiCode.INVALID_PARAM);
            }
        }else if(recommendForm.getObjectType() == ObjectType.COMPANY.value
                || recommendForm.getObjectType() == ObjectType.COMPANY_WIKI.value){
             CompanyVo companyVo = companyService.getCompanyInfo(recommendForm.getObjectId());
             if(companyVo == null){
                 throw new ServiceException(ApiCode.INVALID_PARAM);
             }
        }

        Recommend recommend = null;
        if(recommendForm.getId() != null){
            recommend = recommendMapper.selectByPrimaryKey(recommendForm.getId());
        }

        if(recommend == null){
            recommend = new Recommend();
        }

        BeanUtil.copyProperties(recommendForm, recommend);
        if(recommend.getId() != null){
            recommend.setUpdateTime(new Date());
            recommendMapper.updateByPrimaryKeySelective(recommend);
        }else{
            recommendMapper.insertSelective(recommend);
        }
    }

    /**
     * 删除推荐
     *
     * @param id
     */
    public void deleteRecommend(Integer id){
        recommendMapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取精选工作
     *
     * @return
     */
    public PageResult<RecommendVo> getRecommendList(RecommendListForm listForm){
        Recommend options = new Recommend();
        options.setObjectType(listForm.getObjectType());
        AppUtils.setPage(listForm);
        Page<Recommend> recommendPage = (Page<Recommend>) recommendMapper.selectAndList(options);
        PageResult<RecommendVo> pageResult = new PageResult(recommendPage);
        List<RecommendVo> recommendVoList = new ArrayList<>(recommendPage.size());
        for(Recommend recommend: recommendPage){
            RecommendVo recommendVo = new RecommendVo();
            recommendVo.setId(recommend.getId());
            recommendVo.setPosition(recommend.getPosition());
            recommendVo.setCreateTime(recommend.getCreateTime());
            if(listForm.getObjectType() == ObjectType.JOB.value){
                recommendVo.setObject(jobService.getJobInfo(recommend.getObjectId(), true));
            }else if(listForm.getObjectType() == ObjectType.COMPANY.value || listForm.getObjectType() == ObjectType.COMPANY_WIKI.value){
                recommendVo.setObject(companyService.getCompanyInfo(recommend.getObjectId()));
            }
            recommendVoList.add(recommendVo);
        }
        pageResult.setList(recommendVoList);
        return pageResult;
    }

}
