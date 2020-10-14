package com.worldelite.job.service;

import com.worldelite.job.entity.CompanyUser;
import com.worldelite.job.entity.UserRepository;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.mapper.CompanyUserMapper;
import com.worldelite.job.mapper.UserRepositoryMapper;
import com.worldelite.job.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业简历用户服务类
 */
@Service
public class UserRepositoryService extends BaseService {

    @Autowired
    private UserRepositoryMapper userRepositoryMapper;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    public UserRepository getUserById(Long id){
        return userRepositoryMapper.selectByPrimaryKey(id);
    }

    public void addUser(UserRepository userRepository){
        userRepositoryMapper.insertSelective(userRepository);
    }

    public void saveUser(UserRepository userRepository){
        userRepositoryMapper.updateByPrimaryKeySelective(userRepository);
    }

    /**
     * 创建一个空用户
     * @return
     */
    public UserRepository newUser(){
        UserRepository userRepository = new UserRepository();
        userRepository.setId(AppUtils.nextId());
        //获取当前登录的企业账号对应的企业ID
        Long creatorId = curUser().getId();
        CompanyUser companyUser = companyUserMapper.selectByUserId(creatorId);
        //如果当前登录用户没有对应企业
        //说明数据出现了异常，不能再继续处理
        if(companyUser == null){
            throw new ServiceException(message("api.error.data.company"));
        }
        userRepository.setCreatorId(creatorId);
        userRepository.setCompanyId(companyUser.getCompanyId());
        return userRepository;
    }

}
