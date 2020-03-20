package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.User;
import com.worldelite.job.form.CompanyAddressForm;
import com.worldelite.job.form.CompanyForm;
import com.worldelite.job.form.CompanyListForm;
import com.worldelite.job.form.CompanyVerifyForm;
import com.worldelite.job.service.CompanyAddressService;
import com.worldelite.job.service.CompanyService;
import com.worldelite.job.service.CompanyVerificationService;
import com.worldelite.job.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/company")
@Validated
public class CompanyApi extends BaseApi {

    @Autowired
    private CompanyService companyService;


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
     * 获取公司列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("list")
    public ApiResult listCompany(CompanyListForm listForm){
        PageResult pageResult = companyService.getCompanyList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取公司信息
     * @param id
     * @return
     */
    @GetMapping("/company-home-data")
    public ApiResult<CompanyVo> getCompanyHomeData(@RequestParam Long id){
        CompanyVo companyVo = companyService.getCompanyHomeData(id);
        return ApiResult.ok(companyVo);
    }

    /**
     * 获取用户公司详情
     *
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("my-company-info")
    public ApiResult<CompanyVo> getMyCompanyInfo() {
        CompanyVo companyVo = companyService.getMyCompanyInfo();
        return ApiResult.ok(companyVo);
    }

    /**
     * 获取公司信息
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("get-company-info")
    public ApiResult getCompanyInfo(@RequestParam Long id){
        CompanyVo companyVo = companyService.getCompanyInfo(id);
        return ApiResult.ok(companyVo);
    }

    /**
     * 保存公司信息
     *
     * @return
     */
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
    @PostMapping("save-company")
    public ApiResult saveCompany(@RequestBody CompanyForm companyForm) {
        Long companyId = companyService.saveCompany(companyForm);
        return ApiResult.ok(String.valueOf(companyId));
    }

    /**
     * 删除公司
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("del-company")
    public ApiResult delCompany(@RequestParam Long id){
        companyService.delCompany(id);
        return ApiResult.ok();
    }

    /**
     * 保存公司地址
     *
     * @param companyAddressForm
     * @return
     */
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
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
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
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
