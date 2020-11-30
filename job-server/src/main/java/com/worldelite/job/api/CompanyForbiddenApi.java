package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.CompanyForbidden;
import com.worldelite.job.entity.CompanyScore;
import com.worldelite.job.form.*;
import com.worldelite.job.service.CompanyForbiddenService;
import com.worldelite.job.service.CompanyScoreService;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 企业评分接口
 */
@RestController
@RequestMapping("/api/company-forbidden/")
@Validated
public class CompanyForbiddenApi extends BaseApi {

    @Autowired
    private CompanyForbiddenService companyForbiddenService;

    /**
     * 禁言或者解禁用户
     * @param companyForbiddenForm
     * @return
     */
    @ApiDoc
    @PostMapping("forbidden")
    @RequireLogin(allow = UserType.ADMIN)
    public ApiResult forbidden(@RequestBody CompanyForbiddenForm companyForbiddenForm){
        companyForbiddenService.forbidden(companyForbiddenForm);
        return ApiResult.ok();
    }

    /**
     * 查询用户是否被禁言
     * @param userId
     * @return
     */
    @ApiDoc
    @GetMapping("get-forbidden")
    @RequireLogin
    public ApiResult<CompanyForbiddenVo> getForbidden(@RequestParam Long userId){
        CompanyForbiddenVo forbidden = companyForbiddenService.getForbidden(userId);
        return ApiResult.ok(forbidden);
    }

    /**
     * 查询自己是否被禁言
     * @return
     */
    @ApiDoc
    @GetMapping("my-forbidden")
    @RequireLogin
    public ApiResult<CompanyForbiddenVo> myForbidden(){
        CompanyForbiddenVo forbidden = companyForbiddenService.myForbidden();
        return ApiResult.ok(forbidden);
    }
}
