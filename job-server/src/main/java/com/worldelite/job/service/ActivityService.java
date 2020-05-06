package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ActivityStatus;
import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.Dict;
import com.worldelite.job.form.ActivityForm;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ActivityService extends BaseService{

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private FavoriteService favoriteService;

    /**
     * 获取活动列表
     * @param listForm
     * @return
     */
    public PageResult<ActivityVo> getActivityList(ActivityListForm listForm){
        Activity options = new Activity();
        BeanUtil.copyProperties(listForm, options);
        AppUtils.setPage(listForm);
        Page<Activity> activityPage = (Page<Activity>) activityMapper.selectAndList(options);
        PageResult<ActivityVo> pageResult = new PageResult<>(activityPage);
        List<ActivityVo> activityVoList = new ArrayList<>(activityPage.size());
        for(Activity activity: activityPage){
            activityVoList.add(toActivityVo(activity));
        }
        pageResult.setList(activityVoList);
        return pageResult;
    }

    /**
     * 获取活动详情
     *
     * @param id
     * @return
     */
    public ActivityVo getActivityInfo(Integer id){
        Activity activity = activityMapper.selectByPrimaryKey(id);
        return toActivityVo(activity);
    }

    public ActivityVo getSimpleActivity(Integer id){
        Activity activity = activityMapper.selectSimpleById(id);
        return toActivityVo(activity);
    }

    /**
     * 保存活动
     *
     * @param activityForm
     * @return
     */
    public Integer saveActivity(ActivityForm activityForm){
        Activity activity = null;
        if(activityForm.getId() != null){
            activity = activityMapper.selectSimpleById(activityForm.getId());
        }

        if(activity == null){
           activity = new Activity();
        }
        BeanUtil.copyProperties(activityForm, activity);
        activity.setThumbnail(AppUtils.getOssKey(activityForm.getThumbnail()));
        if(activity.getId() == null){
            activity.setStatus(ActivityStatus.PUBLISH.value);
            activityMapper.insertSelective(activity);
        }else{
            activity.setUpdateTime(new Date());
            activityMapper.updateByPrimaryKeySelective(activity);
        }
        return activity.getId();
    }

    /**
     * 下架活动
     *
     * @param id
     */
    public void takeOffActivity(Integer id){
        Activity activity = activityMapper.selectSimpleById(id);
        if(activity != null){
            activity.setStatus(ActivityStatus.OFFLINE.value);
            activityMapper.updateByPrimaryKeySelective(activity);
        }
    }

    private ActivityVo toActivityVo(Activity activity){
        if(activity == null){
            return null;
        }
        ActivityVo activityVo = new ActivityVo().asVo(activity);
        activityVo.setCity(dictService.getById(activity.getCityId()));
        if(curUser() != null){
            activityVo.setJoinFlag(favoriteService.checkUserFavorite(activity.getId().longValue(), FavoriteType.ACTIVITY));
        }
        return activityVo;
    }
}
