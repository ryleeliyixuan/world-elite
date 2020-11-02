package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.entity.Favorite;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.FavoriteForm;
import com.worldelite.job.form.FavoriteListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.FavoriteMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import me.zhyd.oauth.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.jsf.FacesContextUtils;

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

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CompanyService companyService;

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
    @Deprecated
    public PageResult<JobVo> getUserFavoriteJobList(PageForm pageForm){
        Favorite options = new Favorite();
        options.setType(FavoriteType.JOB.value);
        options.setUserId(curUser().getId());
        if(StringUtils.isEmpty(pageForm.getSort())){
            pageForm.setSort("-id");
        }
        AppUtils.setPage(pageForm);
        Page<Favorite> favoritePage= (Page<Favorite>)favoriteMapper.selectAndList(options);
        PageResult<JobVo> pageResult = new PageResult<>(favoritePage);
        List<JobVo> jobVoList = new ArrayList<>(favoritePage.size());
        for(Favorite favorite: favoritePage){
            JobVo jobVo = jobService.getJobInfo(favorite.getObjectId(), true);
            jobVo.setFavoriteTime(favorite.getCreateTime());
            jobVoList.add(jobVo);
        }
        pageResult.setList(jobVoList);
        return pageResult;
    }

    /**
     * 获取用户收藏列表
     *
     * @param favoriteListForm
     * @return
     */
    public PageResult getUserFavoriteList(FavoriteListForm favoriteListForm){
        Favorite options = new Favorite();
        options.setType(favoriteListForm.getType());
        options.setUserId(curUser().getId());
        if(StringUtils.isEmpty(favoriteListForm.getSort())){
            favoriteListForm.setSort("-id");
        }
        AppUtils.setPage(favoriteListForm);
        Page<Favorite> favoritePage= (Page<Favorite>)favoriteMapper.selectAndList(options);
        PageResult pageResult = new PageResult<>(favoritePage);
        if(FavoriteType.JOB.value == favoriteListForm.getType()){
            List<JobVo> jobVoList = new ArrayList<>(favoritePage.size());
            for(Favorite favorite: favoritePage){
                JobVo jobVo = jobService.getJobInfo(favorite.getObjectId(), true);
                jobVo.setFavoriteTime(favorite.getCreateTime());
                jobVoList.add(jobVo);
            }
            pageResult.setList(jobVoList);
        }else if(FavoriteType.COMPANY.value == favoriteListForm.getType()){
            List<CompanyVo> companyVoList = new ArrayList<>(favoritePage.size());
            for(Favorite favorite: favoritePage){
                CompanyVo companyVo = companyService.getCompanyInfo(favorite.getObjectId());
                companyVo.setFavoriteTime(favorite.getCreateTime());
                companyVoList.add(companyVo);
            }
            pageResult.setList(companyVoList);
        }
        return pageResult;
    }

    /**
     * 获取用户的活动列表
     * @param pageForm
     * @return
     */
    public PageResult<ActivityVo> getUserActivityList(Long userId, PageForm pageForm){
        Favorite options = new Favorite();
        options.setType(FavoriteType.ACTIVITY.value);
        options.setUserId(userId);
        if(StringUtils.isEmpty(pageForm.getSort())){
            pageForm.setSort("-id");
        }
        AppUtils.setPage(pageForm);
        Page<Favorite> favoritePage= (Page<Favorite>)favoriteMapper.selectAndList(options);
        PageResult<ActivityVo> pageResult = new PageResult<>(favoritePage);
        List<ActivityVo> activityVoList = new ArrayList<>(favoritePage.size());
        for(Favorite favorite: favoritePage){
            ActivityVo activityVo = activityService.getSimpleActivity(favorite.getObjectId().intValue());
            activityVo.setJoinTime(favorite.getCreateTime());
            activityVoList.add(activityVo);
        }
        pageResult.setList(activityVoList);
        return pageResult;
    }

    /**
     * 获取用户指定状态的活动列表
     * @param pageForm
     * @return
     */
    public PageResult<ActivityVo> getUserActivityListByStatus(Long userId, ActivityListForm pageForm){
        return activityService.getSimpleActivityByStatus(userId,pageForm);
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

    /**
     * 获取百科订阅数
     * @param companyId
     * @return
     */
    public Integer getWikiFavoriteCount(Long companyId){
        Favorite options = new Favorite();
        options.setType(FavoriteType.COMPANY.value);
        options.setObjectId(companyId);
        List<Favorite> favoriteList = favoriteMapper.selectAndList(options);
        return favoriteList.size();
    }

    public void save(Favorite favorite){
        favoriteMapper.insertSelective(favorite);
    }

    public void delete(Integer id){
        favoriteMapper.deleteByPrimaryKey(id);
    }

    public List<Favorite> listByObjectId(Long objectId,FavoriteType type){
        Favorite favorite = new Favorite();
        favorite.setUserId(curUser().getId());
        favorite.setType(type.value);
        favorite.setObjectId(objectId);
        return favoriteMapper.selectAndList(favorite);
    }

}
