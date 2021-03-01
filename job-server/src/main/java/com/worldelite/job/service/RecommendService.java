package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ObjectType;
import com.worldelite.job.entity.Recommend;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.feign.MockService;
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

    @Autowired
    private MockService mockService;

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
            //给定排序默认值
            if(recommend.getPosition() == null){
                Integer position = recommendMapper.selectMaxPosition(recommend);
                if(position == null){
                    position = 0;
                }
                recommend.setPosition(position+1);
            }
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
                final JobVo jobInfo = jobService.getJobInfo(recommend.getObjectId(), true);
                if(jobInfo == null) continue;

                recommendVo.setObject(jobInfo);
            }else if(listForm.getObjectType() == ObjectType.COMPANY.value || listForm.getObjectType() == ObjectType.COMPANY_WIKI.value){
                final CompanyVo companyInfo = companyService.getCompanyInfo(recommend.getObjectId());
                if(companyInfo == null) continue;
                
                recommendVo.setObject(companyInfo);
            }else if(listForm.getObjectType() == ObjectType.MOCK.value){
                ApiResult<InterviewerInfoVo> interviewerInfoVo = mockService.getInterviewerInfo(
                        String.valueOf(recommend.getObjectId()));
                if(interviewerInfoVo == null || interviewerInfoVo.getData() == null){
                    continue;
                }
                recommendVo.setObject(interviewerInfoVo.getData());
            }
            recommendVoList.add(recommendVo);
        }
        pageResult.setList(recommendVoList);
        return pageResult;
    }

    public PageResult<CompanyVo> getRecentJobCompany(PageForm pageForm){
        AppUtils.setPage(pageForm);
        Page<Long> companyIdList = (Page<Long>) recommendMapper.selectRecentJobCompanyId();
        PageResult<CompanyVo> pageResult = new PageResult(companyIdList);
        List<CompanyVo> companyVoList = new ArrayList<>();
        for (Long id : companyIdList) {
            CompanyVo companyInfo = companyService.getCompanyInfo(id);
            if (companyInfo != null)
                companyVoList.add(companyInfo);
        }

        pageResult.setList(companyVoList);
        return pageResult;
    }

    /**
     * 上升
     * @param id
     */
    public void moveUp(Integer id){
        Recommend recommend1 = recommendMapper.selectByPrimaryKey(id);
        if(recommend1 == null){
            throw new ServiceException(ApiCode.INVALID_PARAM);
        }
        Recommend options = new Recommend();
        options.setPosition(recommend1.getPosition());
        options.setObjectType(recommend1.getObjectType());
        Recommend recommend2 = recommendMapper.selectPrePosition(options);
        if(recommend2 != null){
            swapPosition(recommend1,recommend2);
        }
    }

    /**
     * 下降
     * @param id
     */
    public void moveDown(Integer id){
        Recommend recommend1 = recommendMapper.selectByPrimaryKey(id);
        if(recommend1 == null){
            throw new ServiceException(ApiCode.INVALID_PARAM);
        }
        Recommend options = new Recommend();
        options.setPosition(recommend1.getPosition());
        options.setObjectType(recommend1.getObjectType());
        Recommend recommend2 = recommendMapper.selectNextPosition(options);
        if(recommend2 != null){
            swapPosition(recommend1,recommend2);
        }
    }

    /**
     * 交换两个推荐信息位置
     * @param recommend1
     * @param recommend2
     */
    private void swapPosition(Recommend recommend1,Recommend recommend2){
        Integer tempPosition = recommend1.getPosition();
        recommend1.setPosition(recommend2.getPosition());
        recommend2.setPosition(tempPosition);
        recommendMapper.updateByPrimaryKeySelective(recommend1);
        recommendMapper.updateByPrimaryKeySelective(recommend2);
    }
}
