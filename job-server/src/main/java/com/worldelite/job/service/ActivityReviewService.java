package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ActivityStatus;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.context.SpringContextHolder;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.ActivityReview;
import com.worldelite.job.event.ActivityInfoRefreshEvent;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ActivityReviewForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.mapper.ActivityReviewMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ActivityReviewVo;
import com.worldelite.job.vo.PageResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ActivityReviewService extends BaseService {
    private final ActivityReviewMapper activityReviewMapper;
    private final ActivityMapper activityMapper;
    private final ActivityStatusManager activityStatusManager;

    public PageResult<ActivityReviewVo> getActivityReviewList(ActivityReviewForm activityReviewForm, PageForm pageForm) {
        ActivityReview options = new ActivityReview();
        BeanUtil.copyProperties(activityReviewForm, options);
        AppUtils.setPage(pageForm);
        Page<ActivityReview> activityReviewPage = (Page<ActivityReview>) activityReviewMapper.selectAndList(options);
        PageResult<ActivityReviewVo> pageResult = new PageResult<>(activityReviewPage);
        pageResult.setList(AppUtils.asVoList(activityReviewPage, ActivityReviewVo.class));
        return pageResult;
    }

    public ActivityReviewVo getActivityReview(Integer id) {
        final ActivityReview activityReview = activityReviewMapper.selectByPrimaryKey(id);
        if (activityReview == null) return null;

        return new ActivityReviewVo().asVo(activityReview);
    }

    public PageResult<ActivityReviewVo> getActivityReviewByActivityId(Integer activityId, PageForm pageForm) {
        AppUtils.setPage(pageForm);
        Page<ActivityReview> activityReviewPage = (Page<ActivityReview>) activityReviewMapper.selectByActivityId(activityId);
        PageResult<ActivityReviewVo> pageResult = new PageResult<>(activityReviewPage);
        pageResult.setList(AppUtils.asVoList(activityReviewPage, ActivityReviewVo.class));
        return pageResult;
    }

    /**
     * 获取活动的最新的审核记录
     * @param activityId
     * @return
     */
    public ActivityReviewVo getActivityReviewNewestByActivityId(Integer activityId) {
        final List<ActivityReview> activityReviews = activityReviewMapper.selectByActivityId(activityId);
        final Optional<ActivityReview> maxActivityReview = activityReviews.stream().max(Comparator.comparingInt(ActivityReview::getId));
        return maxActivityReview.map(activityReview -> new ActivityReviewVo().asVo(activityReview)).orElse(null);
    }

    public Boolean addActivityReview(ActivityReviewForm ActivityReviewForm) {
        ActivityReview activityReview = new ActivityReview();
        BeanUtil.copyProperties(ActivityReviewForm, activityReview);

        if (activityReview.getUserId() == null)
            activityReview.setUserId(curUser().getId());

        activityReview.setStatus(String.valueOf(VerificationStatus.REVIEWING.value));
        return activityReviewMapper.insertSelective(activityReview) == 1;
    }

    public Boolean updateActivityReview(ActivityReviewForm ActivityReviewForm) {
        ActivityReview activityReview = new ActivityReview();
        BeanUtil.copyProperties(ActivityReviewForm, activityReview);

        if (activityReview.getUserId() == null)
            activityReview.setUserId(curUser().getId());

        return activityReviewMapper.updateByPrimaryKeySelective(activityReview) == 1;
    }

    public Boolean delActivityReview(Integer id) {
        return activityReviewMapper.deleteByPrimaryKey(id) == 1;
    }

    /**
     * 活动审核通过
     *
     * @param activityId
     * @return
     */
    @Transactional
    public Boolean activityReviewPass(Integer activityId) {

        final Activity activity = activityMapper.selectByPrimaryKey(activityId);
        if (activity == null) {
            throw new ServiceException(message("activity.not.exist"));
        }
        //只允许待审核和失败的活动允许通过
        if (activity.getStatus() != ActivityStatus.REVIEWING.value && activity.getStatus() != ActivityStatus.REVIEW_FAILURE.value) {
            throw new ServiceException(message("activity.status.error"));
        }

        ActivityReview options = new ActivityReview();
        options.setActivityId(activityId);
        options.setStatus(String.valueOf(VerificationStatus.REVIEWING.value));
        final List<ActivityReview> activityReviews = activityReviewMapper.selectAndList(options);
        //没有待审核的记录,构造一条审核通过的记录
        if (activityReviews.size() == 0) {
            ActivityReview activityReview = new ActivityReview();
            activityReview.setActivityId(activity.getId());
            activityReview.setUserId(activity.getUserId());
            activityReview.setStatus(String.valueOf(VerificationStatus.PASS.value));
            activityReviewMapper.insertSelective(activityReview);

        } else {
            activityReviews.forEach(activityReview -> {
                activityReview.setStatus(String.valueOf(VerificationStatus.PASS.value));
                activityReviewMapper.updateByPrimaryKeySelective(activityReview);
            });
        }

        activity.setStatus(ActivityStatus.WILL.value);
        boolean b = activityMapper.updateByPrimaryKeySelective(activity) == 1;
        if (b) {
            activityStatusManager.remove(activity.getId());
            activityStatusManager.put(activity);
            SpringContextHolder.publishEvent(new ActivityInfoRefreshEvent(this, activity.getId()));
        }
        return b;
    }

    /**
     * 活动审核失败
     *
     * @param activityId
     * @param reason
     * @return
     */
    @Transactional
    public Boolean activityReviewFailure(Integer activityId, String reason) {

        final Activity activity = activityMapper.selectByPrimaryKey(activityId);
        if (activity == null) {
            throw new ServiceException(message("activity.not.exist"));
        }
        if (activity.getStatus() != ActivityStatus.REVIEWING.value) {
            throw new ServiceException(message("activity.status.error"));
        }

        ActivityReview options = new ActivityReview();
        options.setActivityId(activityId);
        options.setStatus(String.valueOf(VerificationStatus.REVIEWING.value));
        final List<ActivityReview> activityReviews = activityReviewMapper.selectAndList(options);
        //没有待审核的记录,构造一条审核不通过的记录
        if (activityReviews.size() == 0) {
            ActivityReview activityReview = new ActivityReview();
            activityReview.setActivityId(activity.getId());
            activityReview.setUserId(activity.getUserId());
            activityReview.setStatus(String.valueOf(VerificationStatus.REJECT.value));
            activityReview.setReason(reason);
            activityReviewMapper.insertSelective(activityReview);

        } else {
            activityReviews.forEach(activityReview -> {
                activityReview.setStatus(String.valueOf(VerificationStatus.REJECT.value));
                activityReview.setReason(reason);
                activityReviewMapper.updateByPrimaryKeySelective(activityReview);
            });
        }

        activity.setStatus(ActivityStatus.REVIEW_FAILURE.value);
        return activityMapper.updateByPrimaryKeySelective(activity) == 1;
    }

}
