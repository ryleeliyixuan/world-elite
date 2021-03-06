package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
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
     * 获取指定审核id的审核信息
     *
     * @param id 活动id
     */
    @ApiDoc
    @RequireLogin
    @GetMapping("/{id}")
    public ApiResult<ActivityReviewVo> getActivityReviewById(@PathVariable("id") Integer id) {
        return ApiResult.ok(activityReviewService.getActivityReview(id));
    }

    /**
     * 获取指定活动id的审核信息
     *
     * @param activityId 活动id
     */
    @ApiDoc
    @RequireLogin
    @GetMapping("activity/{activityId}")
    public ApiResult<PageResult<ActivityReviewVo>> getActivityReviewByActivityId(@PathVariable("activityId") Integer activityId, PageForm pageForm) {
        return ApiResult.ok(activityReviewService.getActivityReviewByActivityId(activityId, pageForm));
    }


    @ApiDoc
    @RequireLogin
    @GetMapping("newest/{activityId}")
    public ApiResult<ActivityReviewVo> getActivityReviewNewestByActivityId(@PathVariable("activityId") Integer activityId) {
        return ApiResult.ok(activityReviewService.getActivityReviewNewestByActivityId(activityId));
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
    public ApiResult deleteActivityReview(@PathVariable("id") Integer id) {
        return ApiResult.ok(activityReviewService.delActivityReview(id));
    }

    /**
     * 活动审核通过
     *
     * @param activityId
     * @return
     */
    @ApiDoc
    @RequireLogin(allow = UserType.ADMIN)
    @PatchMapping("pass/{activityId}")
    public ApiResult activityReviewPass(@PathVariable("activityId") Integer activityId) {
        return ApiResult.ok(activityReviewService.activityReviewPass(activityId));
    }


    /**
     * 活动审核失败
     *
     * @param activityId
     * @param reason
     * @return
     */
    @ApiDoc
    @RequireLogin(allow = UserType.ADMIN)
    @PatchMapping("failure")
    public ApiResult activityReviewFailure(Integer activityId, String reason) {
        return ApiResult.ok(activityReviewService.activityReviewFailure(activityId, reason));
    }
}
