package com.worldelite.job.service;

import com.worldelite.job.entity.JobAddress;
import com.worldelite.job.mapper.JobAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 职位地址服务类
 */
@Service
public class JobAddressService extends BaseService {

    @Autowired
    private JobAddressMapper jobAddressMapper;

    public void add(Long jobId,String address,Double latitude,Double longitude){
        JobAddress jobAddress = jobAddressMapper.selectByJobId(jobId);
        if(jobAddress != null){
            jobAddress.setAddress(address);
            jobAddress.setLatitude(latitude);
            jobAddress.setLongitude(longitude);
            jobAddressMapper.updateByPrimaryKeySelective(jobAddress);
            return;
        }
        jobAddress = new JobAddress();
        jobAddress.setAddress(address);
        jobAddress.setJobId(jobId);
        jobAddress.setLatitude(latitude);
        jobAddress.setLongitude(longitude);
        jobAddressMapper.insertSelective(jobAddress);
    }

    public JobAddress getByJobId(Long jobId){
        return jobAddressMapper.selectByJobId(jobId);
    }
}
