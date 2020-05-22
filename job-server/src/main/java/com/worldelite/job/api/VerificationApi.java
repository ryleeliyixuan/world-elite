package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.form.CompanyVerifyForm;
import com.worldelite.job.form.StatusForm;
import com.worldelite.job.service.CompanyVerificationService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CompanyVerificationVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 企业验证接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/verify/")
@Validated
public class VerificationApi extends BaseApi{

    @Autowired
    private CompanyVerificationService companyVerificationService;

    /**
     * 提交验证信息
     *
     * @param companyVerifyForm
     * @return
     */
    @RequireLogin
    @PostMapping("save-verify-info")
    @ApiDoc
    public ApiResult saveVerifyInfo(@Valid @RequestBody CompanyVerifyForm companyVerifyForm) {
        companyVerificationService.saveVerification(companyVerifyForm);
        return ApiResult.ok();
    }

    /**
     * 获取我的验证信息
     *
     * @return
     */
    @RequireLogin
    @GetMapping("my-verify-info")
    @ApiDoc
    public ApiResult<CompanyVerificationVo> myVerifyInfo() {
        CompanyVerificationVo companyVerificationVo = companyVerificationService.getVerificationInfo(curUser().getId());
        return ApiResult.ok(companyVerificationVo);
    }

    /**
     * 获取审核资料
     *
     * @param userId 用户Id
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("get-verify-info")
    @ApiDoc
    public ApiResult<CompanyVerificationVo> getVerifyInfo(@RequestParam Long userId){
        CompanyVerificationVo companyVerificationVo = companyVerificationService.getVerificationInfo(userId);
        return ApiResult.ok(companyVerificationVo);
    }

    /**
     * 企业用户审核通过或者拒绝
     *
     * @param statusForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("verify-company-user")
    @ApiDoc
    public ApiResult CompanyUser(@RequestBody StatusForm statusForm){
        if(statusForm.getStatus() == VerificationStatus.PASS.value){
            companyVerificationService.passVerification(statusForm.getUserId());
        }else{
            companyVerificationService.rejectVerification(statusForm.getUserId(), statusForm.getReason());
        }
        return ApiResult.ok();
    }

}
