package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.OrganizerCreditForm;
import com.worldelite.job.service.OrganizerCreditService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.OrganizerCreditVo;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 活动举办方信用等级
 *
 * @author Xiang Chao
 **/
@RestController
@RequestMapping("/api/activity/organizercredit")
@AllArgsConstructor
public class OrganizerCreditApi {

    private final OrganizerCreditService organizerCreditService;

    /**
     * 获取信用等级列表
     *
     * @param organizerCreditForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @GetMapping
    public ApiResult<PageResult<OrganizerCreditVo>> getOrganizerCreditList(OrganizerCreditForm organizerCreditForm) {
        return ApiResult.ok(organizerCreditService.getOrganizerCreditList(organizerCreditForm));
    }

    /**
     * 获取指定用户id的信用等级vo
     *
     * @param userId 用户id
     */
    @ApiDoc
    @RequireLogin
    @GetMapping("/{userId}")
    public ApiResult<OrganizerCreditVo> getOrganizerCredit(@PathVariable("userId") Long userId) {
        return ApiResult.ok(organizerCreditService.getOrganizerCredit(userId));
    }

    /**
     * 添加信用等级白名单
     *
     * @param userId
     * @param credit
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PostMapping
    public ApiResult addOrganizerCredit(@RequestBody Long userId, @RequestBody Byte credit) {
        return organizerCreditService.addOrganizerCredit(userId, credit) ? ApiResult.ok() : ApiResult.fail("");
    }

    /**
     * 更新信用等级白名单
     *
     * @param userId
     * @param credit
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PatchMapping
    public ApiResult updateOrganizerCredit(@RequestBody Long userId, @RequestBody Byte credit) {
        return organizerCreditService.updateOrganizerCredit(userId, credit) ? ApiResult.ok() : ApiResult.fail("");
    }

    /**
     * 删除信用等级白名单
     *
     * @param userId
     * @return
     */
    @ApiDoc
    @RequireLogin
    @DeleteMapping("/{userId}")
    public ApiResult delOrganizerCredit(@PathVariable("userId") Long userId) {
        return organizerCreditService.delOrganizerCredit(userId) ? ApiResult.ok() : ApiResult.fail("");
    }
}
