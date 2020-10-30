package com.worldelite.job.api;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.ObjectType;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.*;
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

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private CompanyHistoryService companyHistoryService;

    @Autowired
    private CompanyMarketService companyMarketService;

    @Autowired
    private CompanyProductService companyProductService;

    @Autowired
    private CompanyStructureService companyStructureService;

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
     * 根据行业分组的百科列表
     *
     * @return
     */
    @ApiDoc
    @GetMapping("list-wiki-by-industry")
    public ApiResult<PageResult<CompanyVo>> listGroupCompanyWiki(CompanyWikiListForm listForm){
        PageResult pageResult = companyService.searchCompanyWikiWithIndustry(listForm);
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

    /**
     * 新增百科雇员人数
     * @param companyEmployeeForm 雇员人数表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-employee")
    public ApiResult<CompanyEmployeeVo> saveEmployee(@RequestBody CompanyEmployeeForm companyEmployeeForm){
        CompanyEmployee employee = companyEmployeeService.save(companyEmployeeForm);
        return ApiResult.ok(companyEmployeeService.toVo(employee));
    }

    /**
     * 删除百科雇员人数
     * @param employeeId 雇员人数ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-employee")
    public ApiResult deleteEmployee(@RequestParam Integer employeeId){
        companyEmployeeService.deleteById(employeeId);
        return ApiResult.ok();
    }

    /**
     * 查询公司雇员人数
     * @param companyId 公司雇员人数ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-employee")
    public ApiResult<List<CompanyEmployeeVo>> listEmployee(@RequestParam Long companyId){
        List<CompanyEmployeeVo> employeeVoList = companyEmployeeService.listEmployeeVo(companyId);
        return ApiResult.ok(employeeVoList);
    }

    /**
     * 新增百科发展路径
     * @param companyHistoryForm 发展路径表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-history")
    public ApiResult<CompanyHistoryVo> saveHistory(@RequestBody CompanyHistoryForm companyHistoryForm){
        CompanyHistory history = companyHistoryService.save(companyHistoryForm);
        return ApiResult.ok(companyHistoryService.toVo(history));
    }

    /**
     * 删除百科发展路径
     * @param historyId 发展路径ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-history")
    public ApiResult deleteHistory(@RequestParam Integer historyId){
        companyHistoryService.deleteById(historyId);
        return ApiResult.ok();
    }

    /**
     * 查询公司发展路径
     * @param companyId 公司发展路径ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-history")
    public ApiResult<List<CompanyHistoryVo>> listHistory(@RequestParam Long companyId){
        List<CompanyHistoryVo> historyVoList = companyHistoryService.listHistoryVo(companyId);
        return ApiResult.ok(historyVoList);
    }

    /**
     * 新增百科市值
     * @param companyMarketForm 市值表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-market")
    public ApiResult<CompanyMarketVo> saveMarket(@RequestBody CompanyMarketForm companyMarketForm){
        CompanyMarket market = companyMarketService.save(companyMarketForm);
        return ApiResult.ok(companyMarketService.toVo(market));
    }

    /**
     * 删除百科市值
     * @param marketId 市值ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-market")
    public ApiResult deleteMarket(@RequestParam Integer marketId){
        companyMarketService.deleteById(marketId);
        return ApiResult.ok();
    }

    /**
     * 查询公司市值
     * @param companyId 公司市值ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-market")
    public ApiResult<CompanyMarketVo> listMarket(@RequestParam Long companyId){
        CompanyMarket market = companyMarketService.getByCompanyId(companyId);
        return ApiResult.ok(companyMarketService.toVo(market));
    }

    /**
     * 新增百科产品
     * @param companyProductForm 产品表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-product")
    public ApiResult<CompanyProductVo> saveProduct(@RequestBody CompanyProductForm companyProductForm){
        CompanyProduct product = companyProductService.save(companyProductForm);
        return ApiResult.ok(companyProductService.toVo(product));
    }

    /**
     * 删除百科产品
     * @param productId 产品ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-product")
    public ApiResult deleteProduct(@RequestParam Integer productId){
        companyProductService.deleteById(productId);
        return ApiResult.ok();
    }

    /**
     * 查询公司产品
     * @param companyId 公司产品ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-product")
    public ApiResult<List<CompanyProductVo>> listProduct(@RequestParam Long companyId){
        List<CompanyProductVo> productVoList = companyProductService.listProductVo(companyId);
        return ApiResult.ok(productVoList);
    }

    /**
     * 新增百科组织架构
     * @param companyStructureForm 组织架构表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-structure")
    public ApiResult<CompanyStructureVo> saveStructure(@RequestBody CompanyStructureForm companyStructureForm){
        CompanyStructure structure = companyStructureService.save(companyStructureForm);
        return ApiResult.ok(companyStructureService.toVo(structure));
    }

    /**
     * 删除百科组织架构
     * @param structureId 组织架构ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-structure")
    public ApiResult deleteStructure(@RequestParam Integer structureId){
        companyStructureService.deleteById(structureId);
        return ApiResult.ok();
    }

    /**
     * 查询公司组织架构
     * @param companyId 公司组织架构ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-structure")
    public ApiResult<List<CompanyStructureVo>> listStructure(@RequestParam Long companyId){
        List<CompanyStructureVo> structureVoList = companyStructureService.getStructureTree(companyId);
        return ApiResult.ok(structureVoList);
    }

    /**
     * 改变模块启用状态
     * @param companyWikiForm 百科表单
     * @return
     */
    @ApiDoc
    @PostMapping("change-module-enable")
    public ApiResult<CompanyWikiVo> changeModuleEnable(@RequestBody CompanyWikiForm companyWikiForm){
        CompanyWiki companyWiki = companyWikiService.changeModuleEnable(companyWikiForm);
        return ApiResult.ok(companyWikiService.toVo(companyWiki));
    }
}
