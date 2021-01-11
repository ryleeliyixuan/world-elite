package com.worldelite.job.service;

import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.UserExpectJobForm;
import com.worldelite.job.mapper.UserExpectJobMapper;
import com.worldelite.job.mapper.UserExpectPlaceMapper;
import com.worldelite.job.mapper.UserExpectSalaryMapper;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.CityVo;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.UserExpectJobVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class UserExpectJobService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

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
    public UserExpectJobVo getUserExpectJob(Long userId) {
        UserExpectJobVo userExpectJobVo = new UserExpectJobVo();
        UserExpectJob expectJobOptions = new UserExpectJob();
        expectJobOptions.setUserId(userId);
        List<UserExpectJob> userExpectJobList = expectJobMapper.selectAndList(expectJobOptions);
        List<JobCategoryVo> categoryVoList = new ArrayList<>(userExpectJobList.size());
        if (CollectionUtils.isNotEmpty(userExpectJobList)) {
            for (UserExpectJob userExpectJob : userExpectJobList) {
                categoryVoList.add(jobCategoryService.getById(userExpectJob.getCategoryId()));
            }
            userExpectJobVo.setExpectWorkType(userExpectJobList.get(0).getExpectWorkType());
        }
        userExpectJobVo.setCategoryList(categoryVoList);
        UserExpectPlace expectPlaceOptions = new UserExpectPlace();
        expectPlaceOptions.setUserId(userId);
        List<UserExpectPlace> userExpectPlaceList = expectPlaceMapper.selectAndList(expectPlaceOptions);
        List<CityVo> cityVoList = new ArrayList<>(userExpectPlaceList.size());
        if (CollectionUtils.isNotEmpty(userExpectPlaceList)) {
            for (UserExpectPlace userExpectPlace : userExpectPlaceList) {
                cityVoList.add(cityService.getCityVo(userExpectPlace.getCityId()));
            }
        }
        userExpectJobVo.setCityList(cityVoList);

        UserExpectSalary userExpectSalary = expectSalaryMapper.selectByUserId(userId);
        if (userExpectSalary != null) {
            userExpectJobVo.setSalary(dictService.getById(userExpectSalary.getSalaryId()));
        }

        return userExpectJobVo;
    }

    /**
     * 获取意向职位
     *
     * @param userId
     * @return
     */
    public List<JobCategory> getExpectCategoryList(Long userId) {
        UserExpectJob expectJobOptions = new UserExpectJob();
        expectJobOptions.setUserId(userId);
        List<UserExpectJob> userExpectJobList = expectJobMapper.selectAndList(expectJobOptions);
        List<JobCategory> categoryList = new ArrayList<>(userExpectJobList.size());
        if (CollectionUtils.isNotEmpty(userExpectJobList)) {
            for (UserExpectJob userExpectJob : userExpectJobList) {
                categoryList.add(jobCategoryService.getCategory(userExpectJob.getCategoryId()));
            }
        }
        return categoryList;
    }

    /**
     * 意向城市
     *
     * @param userId
     * @return
     */
    public List<City> getExpectCityList(Long userId) {
        UserExpectPlace expectPlaceOptions = new UserExpectPlace();
        expectPlaceOptions.setUserId(userId);
        List<UserExpectPlace> userExpectPlaceList = expectPlaceMapper.selectAndList(expectPlaceOptions);
        List<City> cityList = new ArrayList<>(userExpectPlaceList.size());
        if (CollectionUtils.isNotEmpty(userExpectPlaceList)) {
            for (UserExpectPlace userExpectPlace : userExpectPlaceList) {
                City city = cityService.getById(userExpectPlace.getCityId());
                if (city == null) {
                    throw new ServiceException(message("api.error.data.city"));
                }
                cityList.add(city);
            }
        }
        return cityList;
    }

    /**
     * 意向薪资
     *
     * @param userId
     * @return
     */
    public Dict getSalary(Long userId) {
        UserExpectSalary expectSalary = expectSalaryMapper.selectByUserId(userId);
        if (expectSalary != null)
            return dictService.getDict(expectSalary.getSalaryId());
        else
            return null;
    }

    /**
     * 保存用户求职意向
     *
     * @param userExpectJobForm
     * @return
     */
    @Transactional
    public UserExpectJobVo saveUserExpectJob(UserExpectJobForm userExpectJobForm) {
        Long userId = userExpectJobForm.getUserId();
        expectPlaceMapper.deleteByUserId(userId);
        if (ArrayUtils.isNotEmpty(userExpectJobForm.getCityIds())) {
            for (Integer cityId : userExpectJobForm.getCityIds()) {
                UserExpectPlace userExpectPlace = new UserExpectPlace();
                userExpectPlace.setUserId(userId);
                userExpectPlace.setCityId(cityId);
                expectPlaceMapper.insertSelective(userExpectPlace);
            }
        }
        expectJobMapper.deleteByUserId(userId);
        if (ArrayUtils.isNotEmpty(userExpectJobForm.getCategoryIds())) {
            for (Integer categoryId : userExpectJobForm.getCategoryIds()) {
                UserExpectJob userExpectJob = new UserExpectJob();
                userExpectJob.setUserId(userId);
                userExpectJob.setCategoryId(categoryId);
                userExpectJob.setExpectWorkType(userExpectJobForm.getExpectWorkType());
                expectJobMapper.insertSelective(userExpectJob);
            }
        }
        expectSalaryMapper.deleteByUserId(userId);
        if (userExpectJobForm.getSalaryId() != null) {
            UserExpectSalary userExpectSalary = new UserExpectSalary();
            userExpectSalary.setUserId(userId);
            userExpectSalary.setSalaryId(userExpectJobForm.getSalaryId());
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

    public String getExpectWorkType(Long id) {
        UserExpectJob userExpectJob = new UserExpectJob();
        userExpectJob.setUserId(id);
        Optional<List<UserExpectJob>> optionalList = Optional.ofNullable(expectJobMapper.selectAndList(userExpectJob));
        return optionalList.isPresent() && optionalList.get().size()!=0 ? optionalList.get().get(0).getExpectWorkType() : "";
    }
}
