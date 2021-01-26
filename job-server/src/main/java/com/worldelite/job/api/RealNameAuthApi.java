package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.RealNameAuthForm;
import com.worldelite.job.service.RealNameAuthService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.RealNameAuthVo;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 实名认证审核接口
 *
 * @author Xiang Chao
 **/
@RestController
@RequestMapping("/api/realnameauth")
@AllArgsConstructor
public class RealNameAuthApi {
    private final RealNameAuthService realNameAuthService;

    /**
     * 获取实名认证审核列表
     *
     * @param realNameAuthForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @GetMapping
    public ApiResult<PageResult<RealNameAuthVo>> getRealNameAuthList(RealNameAuthForm realNameAuthForm) {
        return ApiResult.ok(realNameAuthService.getRealNameAuthList(realNameAuthForm));
    }

    /**
     * 获取指定审核id的审核信息
     *
     * @param userId 实名认证用户id
     */
    @ApiDoc
    @RequireLogin
    @GetMapping("/{userId}")
    public ApiResult<RealNameAuthVo> getRealNameAuth(@PathVariable("userId") Long userId) {
        return ApiResult.ok(realNameAuthService.getRealNameAuth(userId));
    }


    /**
     * 添加新实名认证审核
     *
     * @param realNameAuthForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PostMapping
    public ApiResult addRealNameAuth(@RequestBody RealNameAuthForm realNameAuthForm) {
        return ApiResult.ok(realNameAuthService.addRealNameAuth(realNameAuthForm));
    }

    /**
     * 更新实名认证审核
     *
     * @param realNameAuthForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PatchMapping
    public ApiResult updateRealNameAuth(@RequestBody RealNameAuthForm realNameAuthForm) {
        return ApiResult.ok(realNameAuthService.updateRealNameAuth(realNameAuthForm));
    }

    /**
     * 删除实名认证审核
     *
     * @param userId 实名认证用户id
     */
    @ApiDoc
    @RequireLogin
    @DeleteMapping("/{userId}")
    public ApiResult delRealNameAuth(@PathVariable("userId") Long userId) {
        return ApiResult.ok(realNameAuthService.delRealNameAuth(userId));
    }

    /**
     * 实名认证审核通过
     *
     * @param userId
     * @return
     */
    @ApiDoc
    @RequireLogin(allow = UserType.ADMIN)
    @PatchMapping("pass")
    public ApiResult realNameAuthPass(Long userId) {
        return ApiResult.ok(realNameAuthService.realNameAuthPass(userId));
    }


    /**
     * 实名认证审核失败
     *
     * @param userId
     * @param reason
     * @return
     */
    @ApiDoc
    @RequireLogin(allow = UserType.ADMIN)
    @PatchMapping("failure")
    public ApiResult realNameAuthFailure(Long userId, String reason) {
        return ApiResult.ok(realNameAuthService.realNameAuthFailure(userId, reason));
    }
}
