package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.ActivityForm;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.service.ActivityService;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/activity")
@Validated
public class ActivityApi {

    @Autowired
    private ActivityService activityService;

    @GetMapping("list")
    public ApiResult getActivityList(ActivityListForm listForm) {
        PageResult pageResult = activityService.getActivityList(listForm);
        return ApiResult.ok(pageResult);
    }

    @GetMapping("activity-info")
    public ApiResult getActivityInfo(@RequestParam Integer id){
        ActivityVo activityVo = activityService.getActivityInfo(id);
        return ApiResult.ok(activityVo);
    }

    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("save")
    public ApiResult saveActivity(@Valid @RequestBody ActivityForm activityForm) {
        activityService.saveActivity(activityForm);
        return ApiResult.ok();
    }

    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("takeoff")
    public ApiResult takeOffActivity(@RequestParam Integer id){
        activityService.takeOffActivity(id);
        return ApiResult.ok();
    }

}
