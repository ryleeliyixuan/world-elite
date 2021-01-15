package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.*;
import com.worldelite.job.context.SpringContextHolder;
import com.worldelite.job.entity.*;
import com.worldelite.job.event.ActivityInfoRefreshEvent;
import com.worldelite.job.event.ActivityTakeOffEvent;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ActivityForm;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.ActivityReviewForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.mapper.ActivityTakeOffMapper;
import com.worldelite.job.mapper.FavoriteMapper;
import com.worldelite.job.mapper.RegistrationMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.OrganizerInfoVo;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.QuestionnaireTemplateVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ActivityService extends BaseService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ActivityStatusManager activityStatusManager;

    @Autowired
    private ActivityReviewService activityReviewService;

    @Autowired
    private OrganizerInfoService organizerInfoService;

    @Autowired
    private ActivityQuestionnaireService activityQuestionnaireService;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private ActivityTakeOffMapper activityTakeOffMapper;

    /**
     * 获取活动列表
     *
     * @param listForm
     * @return
     */
    public PageResult<ActivityVo> getActivityList(ActivityListForm listForm) {
        ActivityOptions options = new ActivityOptions();
        BeanUtil.copyProperties(listForm, options);

        options.setCityIds(StringUtils.join(listForm.getCityIds(), ","));

        //海外留学生属于另一个字段,方便前端传递 合并到一起了
        if (StringUtils.isNotBlank(listForm.getPublisherType())) {
            if (!listForm.getPublisherType().equals(String.valueOf(PublisherType.OVERSEAS.value))) {
                options.setUserType(listForm.getPublisherType());
            } else {
                options.setOnlyOverseasStudent(String.valueOf(Bool.TRUE));
            }
        }
        AppUtils.setPage(listForm);
        Page<Activity> activityPage = (Page<Activity>) activityMapper.selectAndList(options);
        PageResult<ActivityVo> pageResult = new PageResult<>(activityPage);
        List<ActivityVo> activityVoList = new ArrayList<>(activityPage.size());
        for (Activity activity : activityPage) {
            activityVoList.add(toActivityVo(activity));
        }
        pageResult.setList(activityVoList);
        return pageResult;
    }

    public ActivityVo getMyDraftActivityInfo() {
        ActivityOptions options = new ActivityOptions();
        options.setUserId(curUser().getId());
        options.setStatus(ActivityStatus.DRAFT.value);
        final List<Activity> activities = activityMapper.selectAndList(options);
        if (activities.size() > 0)
            return toActivityVo(activities.get(0));

        return null;
    }

    /**
     * 获取活动详情
     *
     * @param id
     * @return
     */
    public ActivityVo getActivityInfo(Integer id) {
        Activity activity = activityMapper.selectByPrimaryKey(id);
        return toActivityVo(activity);
    }

    public PageResult<ActivityVo> getSimpleActivityByStatus(Long userId, ActivityListForm pageForm) {
        //根据不同状态构建查询SQL
        StringBuilder where = new StringBuilder();
        where.append("id in (select object_id from t_favorite where type = 3 and user_id = ").append(userId).append(")");
        Byte status = pageForm.getStatus();
        where.append(" and status = '").append(status).append("'");

        AppUtils.setPage(pageForm);
        Page<Activity> page = (Page<Activity>) activityMapper.selectSimpleByIdAndStatus(where.toString());
        PageResult<ActivityVo> pageResult = new PageResult<>(page);
        List<ActivityVo> activityVoList = new ArrayList<>(page.size());
        for (Activity activity : page) {
            ActivityVo activityVo = toActivityVo(activity);
            Favorite options = new Favorite();
            options.setType(FavoriteType.ACTIVITY.value);
            options.setUserId(userId);
            options.setObjectId(activity.getId().longValue());
            List<Favorite> favoriteList = favoriteMapper.selectAndList(options);
            if (CollectionUtils.isNotEmpty(favoriteList)) {
                activityVo.setAttentionTime(favoriteList.get(0).getCreateTime().getTime());
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
    @Transactional
    public void saveActivity(ActivityForm activityForm) {
        Activity activity = null;
        if (activityForm.getId() != null) {
            activity = activityMapper.selectByPrimaryKey(activityForm.getId());
        }

        if (activity == null) {
            activity = new Activity();
        }
        BeanUtil.copyProperties(activityForm, activity, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true).setIgnoreProperties("status"));

        activity.setPoster(AppUtils.getOssKey(activityForm.getPoster()));

        if (activity.getUserId() == null) {
            activity.setUserId(curUser().getId());
            activity.setUserType(String.valueOf(curUser().getType()));
        }

        if (activity.getId() == null) {
            //添加组织信息
            if (activityForm.getOrganizerInfoForm() != null) {
                final OrganizerInfoVo organizerInfoVo = organizerInfoService.addOrganizerInfo(activityForm.getOrganizerInfoForm());
                if (organizerInfoVo != null) {
                    activity.setOrganizerId(organizerInfoVo.getId());
                }
            }

            //草稿直接保存
            if (activityForm.getStatus() != null && activityForm.getStatus() == ActivityStatus.DRAFT.value) {
                activity.setStatus(ActivityStatus.DRAFT.value);
                //关联报名表
                QuestionnaireTemplateVo template = activityQuestionnaireService
                        .addActivityQuestionnaireFromTemplate(activityForm.getQuestionnaireType(),activityForm.getQuestionnaireId());
                activity.setQuestionnaireId(template.getId());
                activityMapper.insertSelective(activity);
            } else {
                //新发布的活动状态默认待审核
                activity.setStatus(ActivityStatus.REVIEWING.value);
                //关联报名表
                QuestionnaireTemplateVo template = activityQuestionnaireService
                        .addActivityQuestionnaireFromTemplate(activityForm.getQuestionnaireType(),activityForm.getQuestionnaireId());
                activity.setQuestionnaireId(template.getId());
                activityMapper.insertSelective(activity);

                //添加活动审核信息
                ActivityReviewForm activityReviewForm = new ActivityReviewForm();
                activityReviewForm.setActivityId(activity.getId());
                activityReviewForm.setUserId(activity.getUserId());
                activityReviewService.addActivityReview(activityReviewForm);

                activityStatusManager.put(activity);
            }
        } else {
            activity.setUpdateTime(new Date());

            if (activityForm.getOrganizerInfoForm() != null) {
                final OrganizerInfoVo organizerInfoVo = organizerInfoService.updateOrganizerInfo(activityForm.getOrganizerInfoForm());
                if (organizerInfoVo != null) {
                    activity.setOrganizerId(organizerInfoVo.getId());
                }
            }

            if (activityForm.getStatus() != null) {
                activity.setStatus(activityForm.getStatus());
            } else {
                //原本草稿状态,更新时不带状态,设为待审核
                if (activity.getStatus() == ActivityStatus.DRAFT.value)
                    activity.setStatus(ActivityStatus.REVIEWING.value);

            }

            //活动编辑时不能修改报名表信息
            activity.setQuestionnaireId(null);

            activityMapper.updateByPrimaryKeySelective(activity);

            activityStatusManager.remove(activity.getId());
            activityStatusManager.put(activity);
        }

        SpringContextHolder.publishEvent(new ActivityInfoRefreshEvent(this, activity.getId()));
    }

    /**
     * 下架活动
     *
     * @param id
     */
    @Transactional
    public void takeOffActivity(Integer id, String reason) {
        Activity activity = activityMapper.selectByPrimaryKey(id);
        if (activity != null) {
            //自己发布的或者管理员才能下架活动
            if (!activity.getUserId().equals(curUser().getId()) && curUser().getType() != UserType.ADMIN.value) {
                throw new ServiceException(message("api.error.permission.denied"));
            }
            //草稿,已下架,审核失败无法下架
            if (activity.getStatus() == ActivityStatus.DRAFT.value
                    || activity.getStatus() == ActivityStatus.OFFLINE.value
                    || activity.getStatus() == ActivityStatus.REVIEW_FAILURE.value) {

                throw new ServiceException(message("activity.takeoff.failed"));
            }

            activity.setStatus(ActivityStatus.OFFLINE.value);
            activityMapper.updateByPrimaryKeySelective(activity);
            activityStatusManager.remove(activity.getId());

            ActivityTakeOff activityTakeOff = new ActivityTakeOff();
            activityTakeOff.setActivityId(activity.getId());
            activityTakeOff.setReason(reason);
            activityTakeOffMapper.insertSelective(activityTakeOff);

            //发送通知, 告知所有活动报名者活动下架
            SpringContextHolder.publishEvent(new ActivityTakeOffEvent(this, activity.getId()));
        } else {
            throw new ServiceException(message("activity.not.exist"));
        }
    }

    /**
     * 删除活动
     *
     * @param id
     */
    public void deleteActivity(Integer id) {
        Activity activity = activityMapper.selectByPrimaryKey(id);
        if (activity != null) {
            //自己发布的或者管理员才能删除活动
            if (activity.getUserId().equals(curUser().getId()) || curUser().getType() == UserType.ADMIN.value) {
                activityMapper.deleteByPrimaryKey(id);
                activityStatusManager.remove(activity.getId());

                SpringContextHolder.publishEvent(new ActivityInfoRefreshEvent(this, activity.getId()));
            } else {
                throw new RuntimeException(message("api.error.permission.denied"));
            }
        } else {
            throw new ServiceException(message("activity.not.exist"));
        }
    }

    private ActivityVo toActivityVo(Activity activity) {
        if (activity == null) {
            return null;
        }
        ActivityVo activityVo = new ActivityVo().asVo(activity);

        activityVo.setCity(cityService.getCityVo(activity.getCityId()));
        if (curUser() != null) {
            activityVo.setAttentionFlag(favoriteService.checkUserFavorite(activity.getId().longValue(), FavoriteType.ACTIVITY));
        }

        activityVo.setOrganizerInfoVo(organizerInfoService.getOrganizerInfo(activity.getOrganizerId()));

        if (curUser() != null) {
            final Registration registration = registrationMapper.selectRegistrationStatusByUserId(activity.getId(), curUser().getId());
            if (registration != null) {
                activityVo.setRegistrationFlag(true);
                activityVo.setRegistrationTime(registration.getCreateTime().getTime());
            }
        }

        //如果关联了报名表，则返回报名表名
        Integer questionnaireId = activity.getQuestionnaireId();
        if(questionnaireId != null){
            QuestionnaireTemplateVo template = activityQuestionnaireService.getSimpleActivityQuestionnaire(questionnaireId);
            activityVo.setQuestionnaireName(template.getTitle());
        }

        return activityVo;
    }

    /**
     * 减去一个关注
     */
    public boolean minusFollower(Integer activityId) {
        return activityMapper.minusFollower(activityId) == 1;
    }

    /**
     * 增加一个关注
     */
    public boolean increaseFollower(Integer activityId) {
        return activityMapper.increaseFollower(activityId) == 1;
    }

    /**
     * 减去一个报名人数
     */
    public boolean minusApplicant(Integer activityId) {
        return activityMapper.minusApplicant(activityId) == 1;
    }

    /**
     * 增加一个报名人数
     */
    public boolean increaseApplicant(Integer activityId) {
        return activityMapper.increaseApplicant(activityId) == 1;
    }

    /**
     * 关闭活动通过审核通知提示
     *
     * @param id 活动id
     */
    public boolean closeNotification(Integer id) {
        ActivityOptions options = new ActivityOptions();
        options.setId(id);
        options.setSendNoticeConfirm(String.valueOf(Bool.FALSE));
        return activityMapper.updateByPrimaryKeySelective(options) == 1;
    }
}
