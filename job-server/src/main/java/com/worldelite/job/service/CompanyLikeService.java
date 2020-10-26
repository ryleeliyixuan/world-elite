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
     * 根据当前点赞情况选择点赞或者取消点赞
     * @param ownerId 对象ID
     */
    public void changLike(Long ownerId){
        if(hasLike(ownerId)){
            //取消点赞
            cancel(ownerId);
        }else{
            //点赞
            like(ownerId);
        }
    }

    /**
     * 获取点赞统计数
     * @param ownerId
     * @return
     */
    public Integer getLikeCount(Long ownerId){
        Integer likeCount = companyLikeMapper.countByOwnerId(ownerId);
        return likeCount!=null?likeCount:0;
    }

    /**
     * 点赞
     * @param ownerId
     */
    private void like(Long ownerId){
        CompanyLike like = new CompanyLike();
        like.setOwnerId(ownerId);
        like.setFromId(curUser().getId());
        companyLikeMapper.insertSelective(like);
    }

    /**
     * 取消点赞
     * @param ownerId
     */
    private void cancel(Long ownerId){
        CompanyLike like = new CompanyLike();
        like.setOwnerId(ownerId);
        like.setFromId(curUser().getId());
        companyLikeMapper.deleteSelective(like);
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
     * 删除对象下所有用户点赞信息
     * @param ownerId 对象ID
     */
    public void deleteByOwnerId(Long ownerId){
        companyLikeMapper.deleteByOwnerId(ownerId);
    }
}
