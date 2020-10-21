package com.worldelite.job.service;

import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.UserExpectJobForm;
import com.worldelite.job.mapper.UserExpectJobMapper;
import com.worldelite.job.mapper.UserExpectPlaceMapper;
import com.worldelite.job.mapper.UserExpectSalaryMapper;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.vo.CityVo;
import com.worldelite.job.vo.DictVo;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.UserExpectJobVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private CityService cityService;

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
        List<CityVo> cityVoList = new ArrayList<>(userExpectPlaceList.size());
        if(CollectionUtils.isNotEmpty(userExpectPlaceList)){
            for(UserExpectPlace userExpectPlace: userExpectPlaceList){
                cityVoList.add(cityService.getCityVo(userExpectPlace.getCityId()));
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
     * 获取意向职位
     * @param userId
     * @return
     */
    public List<JobCategory> getExpectCategoryList(Long userId){
        UserExpectJob expectJobOptions = new UserExpectJob();
        expectJobOptions.setUserId(userId);
        List<UserExpectJob> userExpectJobList = expectJobMapper.selectAndList(expectJobOptions);
        List<JobCategory> categoryList = new ArrayList<>(userExpectJobList.size());
        if(CollectionUtils.isNotEmpty(userExpectJobList)){
            for(UserExpectJob userExpectJob: userExpectJobList){
                categoryList.add(jobCategoryService.getCategory(userExpectJob.getCategoryId()));
            }
        }
        return categoryList;
    }

    /**
     * 意向城市
     * @param userId
     * @return
     */
    public List<City> getExpectCityList(Long userId){
        UserExpectPlace expectPlaceOptions = new UserExpectPlace();
        expectPlaceOptions.setUserId(userId);
        List<UserExpectPlace> userExpectPlaceList = expectPlaceMapper.selectAndList(expectPlaceOptions);
        List<City> cityList = new ArrayList<>(userExpectPlaceList.size());
        if(CollectionUtils.isNotEmpty(userExpectPlaceList)){
            for(UserExpectPlace userExpectPlace: userExpectPlaceList){
                City city = cityService.getById(userExpectPlace.getCityId());
                if(city==null){
                    throw new ServiceException(message("api.error.data.city"));
                }
                cityList.add(city);
            }
        }
        return cityList;
    }

    /**
     * 意向薪资
     * Todo 职位薪资已经改成了一个下拉框，意向薪资应该也要做改变，等待产品回复确认，暂时返回null
     * @param userId
     * @return
     */
    public Dict getSalary(Long userId){
        return null;
    }

    /**
     * 保存用户求职意向
     *
     * @param userExpectJobForm
     * @return
     */
    @Transactional
    public UserExpectJobVo saveUserExpectJob(UserExpectJobForm userExpectJobForm){
        Long userId = userExpectJobForm.getUserId();
        expectPlaceMapper.deleteByUserId(userId);
        if(ArrayUtils.isNotEmpty(userExpectJobForm.getCityIds())){
            for(Integer cityId: userExpectJobForm.getCityIds()){
                UserExpectPlace userExpectPlace = new UserExpectPlace();
                userExpectPlace.setUserId(userId);
                userExpectPlace.setCityId(cityId);
                expectPlaceMapper.insertSelective(userExpectPlace);
            }
        }
        expectJobMapper.deleteByUserId(userId);
        if(ArrayUtils.isNotEmpty(userExpectJobForm.getCategoryIds())){
            for(Integer categoryId: userExpectJobForm.getCategoryIds()){
               UserExpectJob userExpectJob = new UserExpectJob();
               userExpectJob.setUserId(userId);
               userExpectJob.setCategoryId(categoryId);
               expectJobMapper.insertSelective(userExpectJob);
            }
        }
        expectSalaryMapper.deleteByUserId(userId);
        if(userExpectJobForm.getMinSalary() != null && userExpectJobForm.getMaxSalary() != null){
            UserExpectSalary userExpectSalary = new UserExpectSalary();
            userExpectSalary.setUserId(userId);
            userExpectSalary.setMinValue(userExpectJobForm.getMinSalary());
            userExpectSalary.setMaxValue(userExpectJobForm.getMaxSalary());
            expectSalaryMapper.insertSelective(userExpectSalary);
        }

//        ResumeVo resumeVo = resumeService.getDefaultOrCreate(userId);
//        Long resumeId = NumberUtils.toLong(resumeVo.getId());
//        if(ArrayUtils.isEmpty(userExpectJobForm.getCityIds()) && ArrayUtils.isEmpty(userExpectJobForm.getCategoryIds())){
//            indexService.deleteResumeItem(resumeId);
//        }else {
//            indexService.saveResumeItem(resumeId);
//        }
        return getUserExpectJob(userId);
    }
}
