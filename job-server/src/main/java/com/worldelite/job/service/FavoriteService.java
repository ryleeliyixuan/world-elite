package com.worldelite.job.service;

import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.entity.Favorite;
import com.worldelite.job.form.FavoriteForm;
import com.worldelite.job.mapper.FavoriteMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class FavoriteService extends BaseService{

    @Autowired
    private FavoriteMapper favoriteMapper;

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
