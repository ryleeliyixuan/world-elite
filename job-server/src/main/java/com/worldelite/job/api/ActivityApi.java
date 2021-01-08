package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.SpringContextHolder;
import com.worldelite.job.event.ActivityInfoRefreshEvent;
import com.worldelite.job.form.ActivityForm;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.SearchNameForm;
import com.worldelite.job.service.ActivitySearchService;
import com.worldelite.job.service.ActivityService;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * 活动接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/activity/")
@Validated
public class ActivityApi extends BaseApi {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivitySearchService activitySearchService;


    /**
     * 活动列表
     *
     * @param listForm
     * @return
     * @throws IOException
     */
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<ActivityVo>> getActivityList(@Valid ActivityListForm listForm) throws IOException {
        PageResult<ActivityVo> pageResult;
        if (listForm.getUserId() == null) {
            pageResult = activitySearchService.searchActivityList(listForm);
        } else {
            pageResult = activityService.getActivityList(listForm);
        }

        return ApiResult.ok(pageResult);
    }

    /**
     * 活动信息
     *
     * @param id 活动ID
     * @return
     */
    @ApiDoc
    @GetMapping("activity-info")
    public ApiResult<ActivityVo> getActivityInfo(@RequestParam Integer id) {
        ActivityVo activityVo = activityService.getActivityInfo(id);
        return activityVo == null ? ApiResult.fail(message("activity.not.exist")) : ApiResult.ok(activityVo);
    }

    /**
     * 获取我的草稿活动
     *
     * @return
     * @throws IOException
     */
    @RequireLogin
    @GetMapping("my/draft-activity-info")
    @ApiDoc
    public ApiResult<PageResult<ActivityVo>> getMyDraftActivityInfo() {
        final ActivityVo myDraftActivityInfo = activityService.getMyDraftActivityInfo();

        return myDraftActivityInfo == null ? ApiResult.fail(message("draft.activity.not.exist")) : ApiResult.ok(myDraftActivityInfo);
    }

    /**
     * 保存活动
     *
     * @param activityForm
     * @return
     */
    @RequireLogin
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveActivity(@Valid @RequestBody ActivityForm activityForm) {
        activityService.saveActivity(activityForm);
        return ApiResult.ok();
    }

    /**
     * 下架活动
     *
     * @param id 活动ID
     * @return
     */
    @RequireLogin
    @PostMapping("takeoff")
    @ApiDoc
    public ApiResult takeOffActivity(@RequestParam Integer id) {
        activityService.takeOffActivity(id);
        return ApiResult.ok();
    }

    /**
     * 关闭活动通过审核通知提示
     *
     * @param id
     * @return
     */
    @RequireLogin
    @PatchMapping("closeSendNotification")
    public ApiResult closeSendNotification(@RequestParam Integer id) {
        final boolean b = activityService.closeNotification(id);
        return b ? ApiResult.ok() : ApiResult.fail(message("activity.not.exist"));
    }

    /**
     * 删除活动
     *
     * @param id 活动ID
     * @return
     */
    @RequireLogin
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteActivity(@RequestParam Integer id) {
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
        activitySearchService.createOrRefreshActivityTitleIndex();
        return ApiResult.ok();
    }


    /**
     * Nuo Xu
     *
     * @param searchNameForm
     * @return
     */
    @ApiDoc
    @GetMapping("search-activity-title")
    public ApiResult<PageResult<String>> searchActivityTitle(SearchNameForm searchNameForm) {
        PageResult<String> result = activitySearchService.searchActivityTitle(searchNameForm);
        return ApiResult.ok(result);
    }

    @PatchMapping("/create-or-refresh-activity-info-index")
    public ApiResult updateActivityInfoIndex() {
        SpringContextHolder.publishEvent(new ActivityInfoRefreshEvent(this));
        return ApiResult.ok();
    }
}
