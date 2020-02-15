package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.CompanyAddressForm;
import com.worldelite.job.form.CompanyForm;
import com.worldelite.job.form.CompanyListForm;
import com.worldelite.job.form.CompanyVerifyForm;
import com.worldelite.job.service.CompanyAddressService;
import com.worldelite.job.service.CompanyService;
import com.worldelite.job.service.CompanyVerificationService;
import com.worldelite.job.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/company")
public class CompanyApi extends BaseApi {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyVerificationService companyVerificationService;

    @Autowired
    private CompanyAddressService companyAddressService;

    /**
     * 搜索公司
     *
     * @param companyListForm
     * @return
     */
    @GetMapping("search")
    public ApiResult<CompanyVo> search(CompanyListForm companyListForm) {
        PageResult pageResult = companyService.search(companyListForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取公司信息
     * @param id
     * @return
     */
    @GetMapping("/company-home-data")
    public ApiResult<CompanyVo> getCompanyInfo(@RequestParam Long id){
        CompanyVo companyVo = companyService.getCompanyHomeData(id);
        return ApiResult.ok(companyVo);
    }

    /**
     * 提交验证信息
     *
     * @param companyVerifyForm
     * @return
     */
    @RequireLogin
    @PostMapping("save-verify-info")
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
    public ApiResult myVerifyInfo() {
        CompanyVerificationVo companyVerificationVo = companyVerificationService.getVerificationInfo(curUser().getId());
        return ApiResult.ok(companyVerificationVo);
    }

    /**
     * 获取用户公司详情
     *
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("my-company-info")
    public ApiResult<CompanyVo> getMyCompanyInfo() {
        CompanyUserVo companyUserVo = companyService.getCompanyUser(curUser().getId());
        CompanyVo companyVo = companyService.getCompanyInfo(Long.valueOf(companyUserVo.getCompany().getId()));
        return ApiResult.ok(companyVo);
    }

    /**
     * 保存公司信息
     *
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save-company")
    public ApiResult saveCompany(@Valid @RequestBody CompanyForm companyForm) {
        companyService.saveCompany(companyForm);
        return ApiResult.ok();
    }

    /**
     * 保存公司地址
     *
     * @param companyAddressForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save-company-addr")
    public ApiResult saveCompanyAddress(@Valid @RequestBody CompanyAddressForm companyAddressForm) {
        companyAddressService.saveCompanyAddress(companyAddressForm);
        return ApiResult.ok();
    }

    /**
     * 保存公司地址
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("del-company-addr")
    public ApiResult delCompanyAddress(@RequestParam Integer id) {
        companyAddressService.delCompanyAddress(id);
        return ApiResult.ok();
    }


    /**
     * 获取当前用户公司地址列表
     *
     * @return
     */
    @GetMapping("list-my-company-addr")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult getCompanyAddressList(){
        CompanyUserVo companyUserVo = companyService.getCompanyUser(curUser().getId());
        Long companyId = Long.valueOf(companyUserVo.getCompany().getId());
        List<CompanyAddressVo> addressVoList = companyAddressService.getCompanyAddressList(companyId);
        return ApiResult.ok(addressVoList);
    }
}
