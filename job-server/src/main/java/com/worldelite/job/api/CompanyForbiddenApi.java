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
     * 禁言用户
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
     * 解禁用户
     * @param companyForbiddenForm
     * @return
     */
    @ApiDoc
    @PostMapping("un-forbidden")
    @RequireLogin(allow = UserType.ADMIN)
    public ApiResult unForbidden(@RequestBody CompanyForbiddenForm companyForbiddenForm){
        companyForbiddenService.unForbidden(companyForbiddenForm);
        return ApiResult.ok();
    }

    /**
     * 批量禁言用户
     * @param companyForbiddenForm
     * @return
     */
    @ApiDoc
    @PostMapping("forbidden-all")
    @RequireLogin(allow = UserType.ADMIN)
    public ApiResult forbiddenAll(@RequestBody CompanyForbiddenForm companyForbiddenForm){
        companyForbiddenService.forbiddenAll(companyForbiddenForm);
        return ApiResult.ok();
    }

    /**
     * 批量解禁用户
     * @param companyForbiddenForm
     * @return
     */
    @ApiDoc
    @PostMapping("un-forbidden-all")
    @RequireLogin(allow = UserType.ADMIN)
    public ApiResult unForbiddenAll(@RequestBody CompanyForbiddenForm companyForbiddenForm){
        companyForbiddenService.unForbiddenAll(companyForbiddenForm);
        return ApiResult.ok();
    }

}
