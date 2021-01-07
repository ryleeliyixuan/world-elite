package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.entity.ActivityReview;
import com.worldelite.job.form.ActivityReviewForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.ActivityReviewMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ActivityReviewVo;
import com.worldelite.job.vo.PageResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ActivityReviewService extends BaseService {
    private final ActivityReviewMapper activityReviewMapper;

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

    public Boolean addActivityReview(ActivityReviewForm ActivityReviewForm) {
        ActivityReview activityReview = new ActivityReview();
        BeanUtil.copyProperties(ActivityReviewForm, activityReview);

        if(activityReview.getUserId() == null)
            activityReview.setUserId(curUser().getId());

        activityReview.setStatus(String.valueOf(VerificationStatus.REVIEWING.value));
        return activityReviewMapper.insertSelective(activityReview) == 1;
    }

    public Boolean updateActivityReview(ActivityReviewForm ActivityReviewForm) {
        ActivityReview activityReview = new ActivityReview();
        BeanUtil.copyProperties(ActivityReviewForm, activityReview);

        if(activityReview.getUserId() == null)
            activityReview.setUserId(curUser().getId());

        return activityReviewMapper.updateByPrimaryKeySelective(activityReview) == 1;
    }

    public Boolean delActivityReview(Integer id) {
        return activityReviewMapper.deleteByPrimaryKey(id) == 1;
    }
}
