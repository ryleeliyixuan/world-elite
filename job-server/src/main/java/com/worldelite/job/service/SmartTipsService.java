package com.worldelite.job.service;

import com.worldelite.job.entity.Job;
import com.worldelite.job.entity.JobCategory;
import com.worldelite.job.mapper.JobCategoryMapper;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.SearchJobTipsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmartTipsService extends BaseService{

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobCategoryMapper jobCategoryMapper;

    /**
     * 职位搜索输入框的智能提示功能
     * 目前只搜索职位名称和职位类型两个类别
     * @param keyword
     * @return
     */
    public SearchJobTipsVo searchJobTips(String keyword){
        //模糊查询职位名称
        List<Job> jobList = jobMapper.selectSmartTips(keyword);
        List<JobVo> jobVoList = new ArrayList<JobVo>();
        for(Job job:jobList){
            jobVoList.add(new JobVo().asVo(job));
        }
        //模糊查询职位类型
        List<JobCategory> jobCategoryList = jobCategoryMapper.selectSmartTips(keyword);
        List<JobCategoryVo> jobCategoryVoList = new ArrayList<JobCategoryVo>();
        for(JobCategory jobCategory:jobCategoryList){
            jobCategoryVoList.add(new JobCategoryVo().asVo(jobCategory));
        }
        return new SearchJobTipsVo().asVo(jobVoList,jobCategoryVoList);
    }

}
