package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ActivityStatus;
import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.ActivityOptions;
import com.worldelite.job.entity.Dict;
import com.worldelite.job.entity.Favorite;
import com.worldelite.job.form.ActivityForm;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.mapper.FavoriteMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    private FavoriteMapper favoriteMapper;

    @Autowired
    private FavoriteService favoriteService;

    /**
     * 获取活动列表
     * @param listForm
     * @return
     */
    public PageResult<ActivityVo> getActivityList(ActivityListForm listForm){
        ActivityOptions options = new ActivityOptions();
        BeanUtil.copyProperties(listForm, options);
        options.setCityIds(StringUtils.join(listForm.getCityIds(), ","));
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

    public PageResult<ActivityVo> getSimpleActivityByStatus(Long userId,ActivityListForm pageForm){
        //根据不同状态构建查询SQL
        StringBuffer where = new StringBuffer();
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String dateStr = sdf.format(date);
        where.append("id in (select object_id from t_favorite where type = 3 and user_id = "+userId+")");
        Byte status = pageForm.getStatus();
        if(status == ActivityStatus.WILL.value){
            where.append(" and start_time > '"+dateStr+"'");
        }
        if(status == ActivityStatus.ACTIVE.value){
            where.append(" and start_time < '"+dateStr+"'");
            where.append(" and finish_time > '"+dateStr+"'");
        }
        if(status == ActivityStatus.END.value){
            where.append(" and finish_time < '"+dateStr+"'");
        }
        AppUtils.setPage(pageForm);
        Page<Activity> page = (Page<Activity>)activityMapper.selectSimpleByIdAndStatus(where.toString());
        PageResult<ActivityVo> pageResult = new PageResult<>(page);
        List<ActivityVo> activityVoList = new ArrayList<>(page.size());
        for(Activity activity: page){
            ActivityVo activityVo = toActivityVo(activity);
            Favorite options = new Favorite();
            options.setType(FavoriteType.ACTIVITY.value);
            options.setUserId(userId);
            options.setObjectId(activity.getId().longValue());
            List<Favorite> favoriteList = favoriteMapper.selectAndList(options);
            if(CollectionUtils.isNotEmpty(favoriteList)){
                activityVo.setJoinTime(favoriteList.get(0).getCreateTime());
            }
            activityVoList.add(activityVo);
        }
        pageResult.setList(activityVoList);
        return pageResult;
    }

    /**
     * 保存活动
     *
     * @param activityForm
     * @return
     */
    public void saveActivity(ActivityForm activityForm){
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

    /**
     * 删除活动
     *
     * @param id
     */
    public void deleteActivity(Integer id){
        activityMapper.deleteByPrimaryKey(id);
    }

    private ActivityVo toActivityVo(Activity activity){
        if(activity == null){
            return null;
        }
        ActivityVo activityVo = new ActivityVo().asVo(activity);
        Byte status = activityVo.getStatus();
        Date date = new Date();
        Date startTime = activityVo.getStartTime();
        Date finishTime = activityVo.getFinishTime();
        if(date.compareTo(startTime)<0){
            activityVo.setStatus(ActivityStatus.WILL.value);
        }
        if(date.compareTo(startTime)>=0 && date.compareTo(startTime)<=0){
            activityVo.setStatus(ActivityStatus.ACTIVE.value);
        }
        if(date.compareTo(finishTime)>0){
            activityVo.setStatus(ActivityStatus.END.value);
        }
        activityVo.setCurTime(new Date());
        activityVo.setCity(dictService.getById(activity.getCityId()));
        if(curUser() != null){
            activityVo.setJoinFlag(favoriteService.checkUserFavorite(activity.getId().longValue(), FavoriteType.ACTIVITY));
        }
        return activityVo;
    }
}
