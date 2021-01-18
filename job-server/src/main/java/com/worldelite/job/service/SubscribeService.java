package com.worldelite.job.service;

import com.worldelite.job.constants.SubscribeType;
import com.worldelite.job.entity.Subscribe;
import com.worldelite.job.mapper.SubscribeMapper;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.SearchJobHistoryVo;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 用户订阅服务类
 */
@Service
public class SubscribeService extends BaseService{

    @Autowired
    private SubscribeMapper subscribeMapper;

    @Autowired
    private JobService jobService;

    /**
     * 订阅职位
     * @param jobId
     */
    public void subscribeJob(Long jobId){
        Subscribe subscribe = new Subscribe();
        subscribe.setUserId(curUser().getId());
        subscribe.setObjectId(jobId);
        subscribe.setObjectType(SubscribeType.JOB.value);
        subscribeMapper.insertSelective(subscribe);
    }

    /**
     * 获取我订阅的职位
     * @return
     */
    public List<JobVo> getMyJobList(){
        Subscribe options = new Subscribe();
        options.setObjectType(SubscribeType.JOB.value);
        options.setUserId(curUser().getId());
        List<Subscribe> subscribeList = subscribeMapper.selectAndList(options);
        List<JobVo> jobVoList = new ArrayList<>();
        for(Subscribe subscribe:subscribeList){
            JobVo jobVo = jobService.getJobDetail(subscribe.getObjectId());
            jobVoList.add(jobVo);
        }
        return jobVoList;
    }

    /**
     * 通过订阅职位生成搜索记录
     * @return
     */
    public SearchJobHistoryVo getMySearchJobHistory(){
        List<JobVo> jobVoList = getMyJobList();
        List<Integer> cityIds = new ArrayList<>();
        List<Integer> degreeIds = new ArrayList<>();;
        List<Integer> salaryIds = new ArrayList<>();;
        List<Integer> industryIds = new ArrayList<>();;
        List<String> cityValues = new ArrayList<>();;
        List<String> degreeValues = new ArrayList<>();;
        List<String> salaryValues = new ArrayList<>();;
        List<String> industryValues = new ArrayList<>();;
        for(JobVo jobVo:jobVoList){
            if(jobVo == null){
                continue;
            }
            if(jobVo.getCity() != null) {
                cityIds.add(jobVo.getCity().getId());
                cityValues.add(jobVo.getCity().getName());
            }
            if(jobVo.getMinDegree() != null) {
                degreeIds.add(jobVo.getMinDegree().getId());
                degreeValues.add(jobVo.getMinDegree().getName());
            }
            if(jobVo.getSalary() != null) {
                salaryIds.add(jobVo.getSalary().getId());
                salaryValues.add(jobVo.getSalary().getName());
            }
            if(jobVo.getJobType() != null) {
                industryIds.add(jobVo.getJobType().getId());
                industryValues.add(jobVo.getJobType().getName());
            }
        }
        SearchJobHistoryVo.SearchJobHistoryVoBuilder builder = SearchJobHistoryVo.builder();
        //去重
        builder.cityIds(new ArrayList<>(new HashSet<>(cityIds)));
        builder.cityValues(new ArrayList<>(new HashSet<>(cityValues)));
        builder.degreeIds(new ArrayList<>(new HashSet<>(degreeIds)));
        builder.degreeValues(new ArrayList<>(new HashSet<>(degreeValues)));
        builder.salaryIds(new ArrayList<>(new HashSet<>(salaryIds)));
        builder.salaryValues(new ArrayList<>(new HashSet<>(salaryValues)));
        builder.industryIds(new ArrayList<>(new HashSet<>(industryIds)));
        builder.salaryValues(new ArrayList<>(new HashSet<>(industryValues)));
        return builder.build();
    }

}
