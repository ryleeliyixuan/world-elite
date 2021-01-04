package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.ActivityReviewForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.ActivityReviewService;
import com.worldelite.job.vo.ActivityReviewVo;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 活动审核接口
 *
 * @author Xiang Chao
 **/
@RestController
@RequestMapping("/api/activity/review")
@AllArgsConstructor
public class ActivityReviewApi {
    private final ActivityReviewService activityReviewService;

    /**
     * 获取活动审核列表
     *
     * @param activityReviewForm
     * @param pageForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @GetMapping
    public ApiResult<PageResult<ActivityReviewVo>> getActivityReviewList(ActivityReviewForm activityReviewForm, PageForm pageForm) {
        return ApiResult.ok(activityReviewService.getActivityReviewList(activityReviewForm, pageForm));
    }

    /**
     * 获取指定活动id的审核信息
     * @param id 活动id
     */
    @ApiDoc
    @RequireLogin
    @GetMapping("/{id}")
    public ApiResult<ActivityReviewVo> getActivityReview(@PathVariable("id") Integer id) {
        return ApiResult.ok(activityReviewService.getActivityReview(id));
    }

    /**
     * 添加新活动审核
     *
     * @param activityReviewForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PostMapping
    public ApiResult addActivityReview(@RequestBody ActivityReviewForm activityReviewForm) {
        return ApiResult.ok(activityReviewService.addActivityReview(activityReviewForm));
    }

    /**
     * 更新活动审核
     *
     * @param activityReviewForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PatchMapping
    public ApiResult updateActivityReview(@RequestBody ActivityReviewForm activityReviewForm) {
        return ApiResult.ok(activityReviewService.updateActivityReview(activityReviewForm));
    }

    /**
     * 删除活动审核
     *
     * @param id 活动id
     */
    @ApiDoc
    @RequireLogin
    @DeleteMapping("/{id}")
    public ApiResult<PageResult<ActivityReviewVo>> deleteActivityReviewList(@PathVariable("id") Integer id) {
        return ApiResult.ok(activityReviewService.delActivityReview(id));
    }
}
