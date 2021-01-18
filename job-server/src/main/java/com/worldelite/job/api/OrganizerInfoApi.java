package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.OrganizerInfoForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.OrganizerInfoService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.OrganizerInfoVo;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 活动组织信息
 *
 * @author Xiang Chao
 **/
@RestController
@RequestMapping("/api/activity/organizer")
@AllArgsConstructor
public class OrganizerInfoApi {

    private final OrganizerInfoService organizerInfoService;

    /**
     * 获取组织信息列表
     *
     * @param organizerInfoForm
     * @param pageForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @GetMapping
    public ApiResult<PageResult<OrganizerInfoVo>> getOrganizerInfoList(OrganizerInfoForm organizerInfoForm, PageForm pageForm) {
        return ApiResult.ok(organizerInfoService.getOrganizerInfoList(organizerInfoForm, pageForm));
    }

    /**
     * 获取指定id的组织信息
     *
     * @param id 活动id
     */
    @ApiDoc
    @RequireLogin
    @GetMapping("/{id}")
    public ApiResult<OrganizerInfoVo> getOrganizerInfo(@PathVariable("id") Integer id) {
        return ApiResult.ok(organizerInfoService.getOrganizerInfo(id));
    }

    /**
     * 添加新组织信息
     *
     * @param organizerInfoForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PostMapping
    public ApiResult addOrganizerInfo(@RequestBody OrganizerInfoForm organizerInfoForm) {
        return ApiResult.ok(organizerInfoService.addOrganizerInfo(organizerInfoForm));
    }

    /**
     * 更新组织信息
     * @param organizerInfoForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PatchMapping
    public ApiResult updateOrganizerInfo(@RequestBody OrganizerInfoForm organizerInfoForm) {
        return ApiResult.ok(organizerInfoService.updateOrganizerInfo(organizerInfoForm));
    }

    /**
     * 删除组织信息
     *
     * @param id 活动id
     */
    @ApiDoc
    @RequireLogin
    @DeleteMapping("/{id}")
    public ApiResult deleteOrganizerInfo(@PathVariable("id") Integer id) {
        return ApiResult.ok(organizerInfoService.delOrganizerInfo(id));
    }
}
