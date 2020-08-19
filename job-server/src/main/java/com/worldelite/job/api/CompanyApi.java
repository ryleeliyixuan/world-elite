package com.worldelite.job.api;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.ObjectType;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.Company;
import com.worldelite.job.entity.CompanyWiki;
import com.worldelite.job.entity.User;
import com.worldelite.job.form.*;
import com.worldelite.job.service.*;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/company/")
@Validated
public class CompanyApi extends BaseApi {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyAddressService companyAddressService;

    @Autowired
    private CompanyWikiService companyWikiService;

    @Autowired
    private RecommendService recommendService;

    /**
     * 搜索公司
     *
     * @param companyListForm
     * @return
     */
    @ApiDoc
    @GetMapping("search")
    public ApiResult<PageResult<CompanyVo>> search(CompanyListForm companyListForm) {
        PageResult pageResult = companyService.search(companyListForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 百科列表
     *
     * @return
     */
    @ApiDoc
    @GetMapping("wiki-list")
    public ApiResult<PageResult<CompanyVo>> listCompanyWiki(CompanyWikiListForm listForm){
        PageResult pageResult;
        if(StringUtils.isNotEmpty(listForm.getKeyword())){
            pageResult = companyService.searchCompanyWiki(listForm);
        }else{
            RecommendListForm recommendListForm = new RecommendListForm();
            BeanUtil.copyProperties(listForm, recommendListForm);
            recommendListForm.setObjectType(ObjectType.COMPANY_WIKI.value);
            PageResult<RecommendVo> recommendPageResult = recommendService.getRecommendList(recommendListForm);
            pageResult = recommendPageResult;
            if(CollectionUtils.isNotEmpty(recommendPageResult.getList())){
                List<CompanyVo> companyVoList = new ArrayList<>(recommendPageResult.getList().size());
                for(RecommendVo<CompanyVo> recommendVo: recommendPageResult.getList()){
                    companyVoList.add(recommendVo.getObject());
                }
                pageResult.setList(companyVoList);
            }
        }
        return ApiResult.ok(pageResult);
    }

    /**
     * 公司列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<CompanyVo>> listCompany(CompanyListForm listForm){
        PageResult pageResult = companyService.getCompanyList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取公司信息
     * @param id 公司ID
     * @return
     */
    @GetMapping("company-home-data")
    public ApiResult<CompanyVo> getCompanyHomeData(@RequestParam Long id){
        CompanyVo companyVo = companyService.getCompanyHomeData(id);
        return ApiResult.ok(companyVo);
    }

    /**
     * 我的公司详情
     *
     * @return
     */
    @ApiDoc
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("my-company-info")
    public ApiResult<CompanyVo> getMyCompanyInfo() {
        CompanyVo companyVo = companyService.getMyCompanyInfo();
        return ApiResult.ok(companyVo);
    }

    /**
     * 获取公司信息
     *
     * @param id 公司ID
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("get-company-info")
    @ApiDoc
    public ApiResult<CompanyVo> getCompanyInfo(@RequestParam Long id){
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
    @ApiDoc
    public ApiResult saveCompany(@RequestBody CompanyForm companyForm) {
        Long companyId = companyService.saveCompany(companyForm);
        return ApiResult.ok(String.valueOf(companyId));
    }

    /**
     * 删除公司
     *
     * @param id 公司ID
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("del-company")
    @ApiDoc
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
    @ApiDoc
    public ApiResult saveCompanyAddress(@Valid @RequestBody CompanyAddressForm companyAddressForm) {
        companyAddressService.saveCompanyAddress(companyAddressForm);
        return ApiResult.ok();
    }

    /**
     * 删除公司地址
     *
     * @param id 地址ID
     * @return
     */
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
    @PostMapping("del-company-addr")
    @ApiDoc
    public ApiResult delCompanyAddress(@RequestParam Integer id) {
        companyAddressService.delCompanyAddress(id);
        return ApiResult.ok();
    }

    /**
     * 获取当前公司地址列表
     *
     * @return
     */
    @GetMapping("list-my-company-addr")
    @RequireLogin(allow = UserType.COMPANY)
    @ApiDoc
    public ApiResult<List<CompanyAddressVo>> getCompanyAddressList(){
        CompanyUserVo companyUserVo = companyService.getCompanyUser(curUser().getId());
        Long companyId = Long.valueOf(companyUserVo.getCompany().getId());
        List<CompanyAddressVo> addressVoList = companyAddressService.getCompanyAddressList(companyId);
        return ApiResult.ok(addressVoList);
    }

    /**
     * 获取公司百科
     * @param companyId 公司ID
     * @return
     */
    @RequireLogin
    @GetMapping("get-company-wiki")
    @ApiDoc
    public ApiResult<CompanyWikiVo> getCompanyWiki(@RequestParam Long companyId){
        CompanyWikiVo companyWikiVo = companyWikiService.getCompanyWiki(companyId);
        return ApiResult.ok(companyWikiVo);
    }

    /**
     * 保存公司百科
     *
     * @param wikiForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("save-company-wiki")
    @ApiDoc
    public ApiResult saveCompanyWiki(@RequestBody CompanyWikiForm  wikiForm){
        companyWikiService.saveCompanyWiki(wikiForm);
        return ApiResult.ok();
    }
}
