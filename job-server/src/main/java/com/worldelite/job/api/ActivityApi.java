package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.ActivityForm;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.SearchNameForm;
import com.worldelite.job.service.ActivityService;
import com.worldelite.job.service.ActivityTitleSearchService;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.alibaba.druid.sql.ast.SQLPartitionValue.Operator.List;

/**
 * 活动接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/activity/")
@Validated
public class ActivityApi {

    @Autowired
    private ActivityService activityService;

    /**
     * Nuo Xu
     */
    @Autowired
    private ActivityTitleSearchService activityTitleSearchService;

    /**
     * 活动列表
     * @param listForm
     * @return
     */
    @ApiDoc
    @PostMapping("list")
    public ApiResult<PageResult<ActivityVo>> getActivityList(@RequestBody ActivityListForm listForm) {
        PageResult<ActivityVo> pageResult = activityService.getActivityList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 活动信息
     * @param id 活动ID
     * @return
     */
    @ApiDoc
    @GetMapping("activity-info")
    public ApiResult<ActivityVo> getActivityInfo(@RequestParam Integer id){
        ActivityVo activityVo = activityService.getActivityInfo(id);
        return ApiResult.ok(activityVo);
    }

    /**
     * 保存活动
     * @param activityForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveActivity(@Valid @RequestBody ActivityForm activityForm) {
        activityService.saveActivity(activityForm);
        return ApiResult.ok();
    }

    /**
     * 下架活动
     * @param id 活动ID
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("takeoff")
    @ApiDoc
    public ApiResult takeOffActivity(@RequestParam Integer id){
        activityService.takeOffActivity(id);
        return ApiResult.ok();
    }

    /**
     * 删除活动
     * @param id 活动ID
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteActivity(@RequestParam Integer id){
        activityService.deleteActivity(id);
        return ApiResult.ok();
    }

    /**
     * Nuo Xu
     * create or refresh activity index
     *
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("create-or-refresh-activity-title-index")
    public ApiResult createOrRefreshActivityTitleIndex() {
        activityTitleSearchService.createOrRefreshActivityTitleIndex();
        return ApiResult.ok();
    }


    /**
     * Nuo Xu
     * @param searchNameForm
     * @return
     */
    @ApiDoc
    @GetMapping("search-activity-title")
    public ApiResult<PageResult<String>> searchActivityTitle(SearchNameForm searchNameForm) {
        activityTitleSearchService.createOrRefreshActivityTitleIndex();
        PageResult<String> result = activityTitleSearchService.searchActivityTitle(searchNameForm);
        return ApiResult.ok(result);
    }
}
