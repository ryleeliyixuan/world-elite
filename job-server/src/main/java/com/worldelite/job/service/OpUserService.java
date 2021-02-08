package com.worldelite.job.service;

import com.worldelite.job.entity.OpUser;
import com.worldelite.job.mapper.OpUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpUserService extends BaseService{

    @Autowired
    private OpUserMapper opUserMapper;

    public boolean isOp(){
        if(curUser() == null){
            return false;
        }
        return isOp(curUser().getId());
    }

    public  boolean isOp(Long userId){
        if(userId == null){
            return false;
        }
        OpUser opUser = opUserMapper.selectByUserId(userId);
        return opUser != null;
    }

}
