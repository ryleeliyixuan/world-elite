package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.ActivityReportForm;
import com.worldelite.job.form.ActivityReportListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.ActivityReportService;
import com.worldelite.job.vo.ActivityReportVo;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 活动举报接口
 *
 * @author Xiang Chao
 **/
@RestController
@RequestMapping("/api/activity/report")
@AllArgsConstructor
public class ActivityReportApi {
    private final ActivityReportService activityReportService;

    /**
     * 获取活动举报列表
     *
     * @param activityReportForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @GetMapping
    public ApiResult<PageResult<ActivityReportVo>> getActivityReportList(ActivityReportListForm activityReportForm) {
        return ApiResult.ok(activityReportService.getActivityReportList(activityReportForm));
    }

    /**
     * 获取指定活动id的举报信息
     *
     * @param id 活动id
     */
    @ApiDoc
    @RequireLogin
    @GetMapping("/{id}")
    public ApiResult<ActivityReportVo> getActivityReport(@PathVariable("id") Integer id) {
        return ApiResult.ok(activityReportService.getActivityReport(id));
    }

    /**
     * 添加新举报
     *
     * @param activityReportForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PostMapping
    public ApiResult addActivityReport(@RequestBody ActivityReportForm activityReportForm) {
        return ApiResult.ok(activityReportService.addActivityReport(activityReportForm));
    }

    /***
     * 更新活动举报
     *
     * @param activityReportForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PatchMapping
    public ApiResult updateActivityReport(@RequestBody ActivityReportForm activityReportForm) {
        return ApiResult.ok(activityReportService.updateActivityReport(activityReportForm));
    }

    /**
     * 删除活动举报
     *
     * @param id 活动id
     */
    @ApiDoc
    @RequireLogin
    @DeleteMapping("/{id}")
    public ApiResult deleteActivityReport(@PathVariable("id") Integer id) {
        return ApiResult.ok(activityReportService.delActivityReport(id));
    }

    @ApiDoc
    @RequireLogin
    @PatchMapping("/turnDownReport")
    public ApiResult turnDownReport(@RequestParam Integer id, String result) {
        return ApiResult.ok(activityReportService.turnDownReport(id, result));
    }
}
