package com.worldelite.job.service;

import com.worldelite.job.entity.Dict;
import com.worldelite.job.entity.JobAddition;
import com.worldelite.job.entity.JobAddress;
import com.worldelite.job.mapper.JobAdditionMapper;
import com.worldelite.job.vo.DictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 职位其他要求服务类
 */
@Service
public class JobAdditionService extends BaseService{

    @Autowired
    private JobAdditionMapper jobAdditionMapper;

    @Autowired
    private DictService dictService;

    public void add(Long jobId,List<Integer> additionIds){
        if(jobId != null && additionIds != null) {
            //删除旧要求数据
            deleteByJobId(jobId);
            //添加新要求数据
            for(Integer additionId:additionIds) {
                JobAddition jobAddition = new JobAddition();
                jobAddition.setJobId(jobId);
                jobAddition.setAdditionId(additionId);
                jobAdditionMapper.insertSelective(jobAddition);
            }
        }
    }

    public List<DictVo> getByJobId(Long jobId){
        List<JobAddition> additionList = jobAdditionMapper.selectByJobId(jobId);
        List<DictVo> list = new ArrayList<>();
        for(JobAddition jobAddition:additionList){
            DictVo dict = dictService.getById(jobAddition.getAdditionId());
            list.add(dict);
        }
        return list;
    }

    private void deleteByJobId(Long jobId){
        jobAdditionMapper.deleteByJobId(jobId);
    }

}
