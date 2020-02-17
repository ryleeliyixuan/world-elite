package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.entity.Favorite;
import com.worldelite.job.form.FavoriteForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.FavoriteMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class FavoriteService extends BaseService{

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private JobService jobService;

    /**
     * 收藏或者取消收藏
     *
     * @param favoriteForm
     * return true 为收藏，false为取消收藏
     */
    public Boolean favorite(FavoriteForm favoriteForm){
        Favorite favorite = new Favorite();
        favorite.setType(favoriteForm.getType());
        favorite.setUserId(curUser().getId());
        favorite.setObjectId(favoriteForm.getObjectId());
        List<Favorite> favoriteList = favoriteMapper.selectAndList(favorite);
        if(favoriteForm.getFavorite()){
            if(CollectionUtils.isEmpty(favoriteList)){
                favoriteMapper.insertSelective(favorite);
            }
        }else{
            if(CollectionUtils.isNotEmpty(favoriteList)){
                favoriteMapper.deleteByPrimaryKey(favoriteList.get(0).getId());
            }
        }
        return !favoriteForm.getFavorite();
    }

    /**
     * 获取当前用户收藏的工作列表
     *
     * @param pageForm
     * @return
     */
    public PageResult<JobVo> getUserFavoriteJobList(PageForm pageForm){
        Favorite options = new Favorite();
        options.setType(FavoriteType.JOB.value);
        options.setUserId(curUser().getId());
        AppUtils.setPage(pageForm);
        Page<Favorite> favoritePage= (Page<Favorite>)favoriteMapper.selectAndList(options);
        PageResult<JobVo> pageResult = new PageResult<>(favoritePage);
        List<JobVo> jobVoList = new ArrayList<>(favoritePage.size());
        for(Favorite favorite: favoritePage){
            jobVoList.add(jobService.getJobInfo(favorite.getObjectId(), true));
        }
        pageResult.setList(jobVoList);
        return pageResult;
    }

    /**
     * 检查用户是否收藏
     *
     * @param objectId
     * @param type
     * @return
     */
    public Boolean checkUserFavorite(Long objectId, FavoriteType type){
        Favorite options = new Favorite();
        options.setType(type.value);
        options.setUserId(curUser().getId());
        options.setObjectId(objectId);
        List<Favorite> favoriteList = favoriteMapper.selectAndList(options);
        return CollectionUtils.isNotEmpty(favoriteList);
    }
}
