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
//
//    @Autowired
//    private UserExpectSalaryMapper expectSalaryMapper;

    @Autowired
    private JobCategoryService jobCategoryService;

//    @Autowired
//    private DictService dictService;
//
    @Autowired
    private IndexService indexService;

    @Autowired
    private CityService cityService;

    /**
     * 获取用户求职意向
     *
     * @param resumeId
     * @return
     */
    public UserExpectJobVo getUserExpectJob(Long resumeId) {
        UserExpectJob expectJobOptions = new UserExpectJob();
        expectJobOptions.setResumeId(resumeId);
        List<UserExpectJob> userExpectJobList = expectJobMapper.selectAndList(expectJobOptions);
        if (userExpectJobList.size()==0){
            return new UserExpectJobVo();
        }
        UserExpectJob userExpectJob = userExpectJobList.get(0);
        JobCategory jobCategory = jobCategoryService.getCategory(userExpectJob.getCategoryId());
        JobCategoryVo jobCategoryVo = new JobCategoryVo().asVo(jobCategory);
        UserExpectJobVo userExpectJobVo = new UserExpectJobVo();

        userExpectJobVo.setCategory(jobCategoryVo);
        userExpectJobVo.setExpectCity(userExpectJob.getExpectCity());
        userExpectJobVo.setExpectPosition(userExpectJob.getExpectPosition());
        userExpectJobVo.setSalaryId(userExpectJob.getSalaryId());

        return userExpectJobVo;
    }

    /**
     * 获取意向职位
     *
     * @param resumeId
     * @return
     */
    public List<JobCategory> getExpectCategoryList(Long resumeId) {
        UserExpectJob expectJobOptions = new UserExpectJob();
        expectJobOptions.setResumeId(resumeId);

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
     * 保存用户求职意向
     *
     * @param userExpectJobForm
     * @return
     */
    @Transactional
    public UserExpectJobVo saveUserExpectJob(UserExpectJobForm userExpectJobForm) {
        Long resumeId = userExpectJobForm.getResumeId();
        UserExpectJob option = new UserExpectJob();
        option.setResumeId(resumeId);
        List<UserExpectJob>list = expectJobMapper.selectAndList(option);
        if (list.size()!=0){
            list.get(0).setCategoryId(userExpectJobForm.getCategoryId());
            list.get(0).setExpectPosition(userExpectJobForm.getExpectPosition());
            list.get(0).setExpectCity(userExpectJobForm.getExpectCity());
            list.get(0).setExpectWorkType(userExpectJobForm.getExpectWorkType());
            list.get(0).setSalaryId(userExpectJobForm.getSalaryId());
            expectJobMapper.updateByPrimaryKeySelective(list.get(0));
        }
        else {
            UserExpectJob record = new UserExpectJob();
            record.setResumeId(resumeId);
            record.setCategoryId(userExpectJobForm.getCategoryId());
            record.setExpectPosition(userExpectJobForm.getExpectPosition());
            record.setExpectCity(userExpectJobForm.getExpectCity());
            record.setExpectWorkType(userExpectJobForm.getExpectWorkType());
            record.setSalaryId(userExpectJobForm.getSalaryId());
            expectJobMapper.insertSelective(record);
        }

        return getUserExpectJob(resumeId);
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
}
