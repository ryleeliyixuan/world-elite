package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.ActivityStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.SpringContextHolder;
import com.worldelite.job.event.ActivityInfoRefreshEvent;
import com.worldelite.job.exception.CheckException;
import com.worldelite.job.form.ActivityForm;
import com.worldelite.job.form.ActivityListAdminForm;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.SearchNameForm;
import com.worldelite.job.service.ActivitySearchService;
import com.worldelite.job.service.ActivityService;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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

    @Qualifier("getValidator")
    @Autowired
    private Validator validator;


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
     * 轮播图
     *
     * @return
     * @throws IOException
     */
    @GetMapping("carousel")
    @ApiDoc
    public ApiResult<List<ActivityVo>> getCarouselList(){
        return ApiResult.ok(activityService.getCarouselList());
    }

    /**
     * 管理端获取活动列表
     *
     * @param listForm
     * @return
     * @throws IOException
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("admin/list")
    @ApiDoc
    public ApiResult<PageResult<ActivityVo>> getActivityList(ActivityListAdminForm listForm) throws IOException {
        return ApiResult.ok(activityService.getActivityList(listForm));
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
        return ApiResult.ok(activityService.getMyDraftActivityInfo());
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
    public ApiResult saveActivity(@RequestBody ActivityForm activityForm) {
        if (activityForm.getStatus() == null || activityForm.getStatus() != ActivityStatus.DRAFT.value) {
            final Set<ConstraintViolation<ActivityForm>> validateSet = validator.validate(activityForm);
            if (validateSet.size() > 0) {
                ConstraintViolation<ActivityForm> model = validateSet.iterator().next();
                throw new CheckException(model.getPropertyPath() + model.getMessage());
            }
        }

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
    public ApiResult takeOffActivity(@RequestParam Integer id, String reason) {
        activityService.takeOffActivity(id, reason);
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
