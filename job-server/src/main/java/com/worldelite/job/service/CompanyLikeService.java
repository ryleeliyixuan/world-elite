package com.worldelite.job.service;

import com.worldelite.job.entity.CompanyLike;
import com.worldelite.job.mapper.CompanyLikeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业点赞服务类
 */
@Slf4j
@Service
public class CompanyLikeService extends BaseService{

    @Autowired
    private CompanyLikeMapper companyLikeMapper;

    /**
     * 改变点赞状态
     * @param ownerId 对象ID
     */
    public void changLike(Long ownerId){
        CompanyLike companyLike = getCompanyLike(ownerId);
        if(companyLike==null){
            //点赞
            newCompanyLike(ownerId);
        }else{
            //取消点赞
            companyLikeMapper.deleteByPrimaryKey(companyLike.getId());
        }
    }

    /**
     * 判断是否点过赞
     * @param ownerId 对象ID
     * @return 是否点过赞
     */
    public Boolean hasLike(Long ownerId){
        return getCompanyLike(ownerId)!=null;
    }

    /**
     * 获取当前用户点赞数据
     * @param ownerId 对象ID
     * @return 点赞数据
     */
    private CompanyLike getCompanyLike(Long ownerId){
        CompanyLike like = new CompanyLike();
        like.setOwnerId(ownerId);
        like.setFromId(curUser().getId());
        return companyLikeMapper.select(like);
    }

    /**
     * 点赞
     * @param ownerId 对象ID
     */
    private void newCompanyLike(Long ownerId){
        CompanyLike like = new CompanyLike();
        like.setOwnerId(ownerId);
        like.setFromId(curUser().getId());
        companyLikeMapper.insertSelective(like);
    }

    /**
     * 删除对象下所有用户点赞信息
     * @param ownerId 对象ID
     */
    private void deleteByOwnerId(Long ownerId){
        companyLikeMapper.deleteByOwnerId(ownerId);
    }
}
