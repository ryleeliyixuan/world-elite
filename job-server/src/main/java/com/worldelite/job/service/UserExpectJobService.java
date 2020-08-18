package com.worldelite.job.service;

import com.worldelite.job.entity.JobCategory;
import com.worldelite.job.entity.UserExpectJob;
import com.worldelite.job.entity.UserExpectPlace;
import com.worldelite.job.entity.UserExpectSalary;
import com.worldelite.job.form.UserExpectJobForm;
import com.worldelite.job.mapper.JobCategoryMapper;
import com.worldelite.job.mapper.UserExpectJobMapper;
import com.worldelite.job.mapper.UserExpectPlaceMapper;
import com.worldelite.job.mapper.UserExpectSalaryMapper;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.vo.DictVo;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.ResumeVo;
import com.worldelite.job.vo.UserExpectJobVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class UserExpectJobService extends BaseService{

    @Autowired
    private UserExpectJobMapper expectJobMapper;

    @Autowired
    private UserExpectPlaceMapper expectPlaceMapper;

    @Autowired
    private UserExpectSalaryMapper expectSalaryMapper;

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private DictService dictService;

    @Autowired
    private IndexService indexService;

    @Autowired
    @Lazy
    private ResumeService resumeService;

    /**
     * 获取用户求职意向
     *
     * @param userId
     * @return
     */
    public UserExpectJobVo getUserExpectJob(Long userId){
        UserExpectJobVo userExpectJobVo = new UserExpectJobVo();
        UserExpectJob expectJobOptions = new UserExpectJob();
        expectJobOptions.setUserId(userId);
        List<UserExpectJob> userExpectJobList = expectJobMapper.selectAndList(expectJobOptions);
        List<JobCategoryVo> categoryVoList = new ArrayList<>(userExpectJobList.size());
        if(CollectionUtils.isNotEmpty(userExpectJobList)){
            for(UserExpectJob userExpectJob: userExpectJobList){
                categoryVoList.add(jobCategoryService.getById(userExpectJob.getCategoryId()));
            }
        }
        userExpectJobVo.setCategoryList(categoryVoList);
        UserExpectPlace expectPlaceOptions = new UserExpectPlace();
        expectPlaceOptions.setUserId(userId);
        List<UserExpectPlace> userExpectPlaceList = expectPlaceMapper.selectAndList(expectPlaceOptions);
        List<DictVo> cityVoList = new ArrayList<>(userExpectPlaceList.size());
        if(CollectionUtils.isNotEmpty(userExpectPlaceList)){
            for(UserExpectPlace userExpectPlace: userExpectPlaceList){
                cityVoList.add(dictService.getById(userExpectPlace.getCityId()));
            }
        }
        userExpectJobVo.setCityList(cityVoList);

        UserExpectSalary userExpectSalary = expectSalaryMapper.selectByUserId(userId);
        if(userExpectSalary != null){
            userExpectJobVo.setMinSalary(userExpectSalary.getMinValue());
            userExpectJobVo.setMaxSalary(userExpectSalary.getMaxValue());
        }
        return userExpectJobVo;
    }

    /**
     * 保存用户求职意向
     *
     * @param userExpectJobForm
     * @return
     */
    @Transactional
    public UserExpectJobVo saveUserExpectJob(UserExpectJobForm userExpectJobForm){
        expectPlaceMapper.deleteByUserId(curUser().getId());
        if(ArrayUtils.isNotEmpty(userExpectJobForm.getCityIds())){
            for(Integer cityId: userExpectJobForm.getCityIds()){
                UserExpectPlace userExpectPlace = new UserExpectPlace();
                userExpectPlace.setUserId(curUser().getId());
                userExpectPlace.setCityId(cityId);
                expectPlaceMapper.insertSelective(userExpectPlace);
            }
        }
        expectJobMapper.deleteByUserId(curUser().getId());
        if(ArrayUtils.isNotEmpty(userExpectJobForm.getCategoryIds())){
            for(Integer categoryId: userExpectJobForm.getCategoryIds()){
               UserExpectJob userExpectJob = new UserExpectJob();
               userExpectJob.setUserId(curUser().getId());
               userExpectJob.setCategoryId(categoryId);
               expectJobMapper.insertSelective(userExpectJob);
            }
        }
        expectSalaryMapper.deleteByUserId(curUser().getId());
        if(userExpectJobForm.getMinSalary() != null && userExpectJobForm.getMaxSalary() != null){
            UserExpectSalary userExpectSalary = new UserExpectSalary();
            userExpectSalary.setUserId(curUser().getId());
            userExpectSalary.setMinValue(userExpectJobForm.getMinSalary());
            userExpectSalary.setMaxValue(userExpectJobForm.getMaxSalary());
            expectSalaryMapper.insertSelective(userExpectSalary);
        }

        ResumeVo resumeVo = resumeService.getDefaultOrCreate(curUser().getId());
        Long resumeId = NumberUtils.toLong(resumeVo.getId());
        if(ArrayUtils.isEmpty(userExpectJobForm.getCityIds()) && ArrayUtils.isEmpty(userExpectJobForm.getCategoryIds())){
            indexService.deleteResumeItem(resumeId);
        }else {
            indexService.saveResumeItem(resumeId);
        }
        return getUserExpectJob(curUser().getId());
    }
}
