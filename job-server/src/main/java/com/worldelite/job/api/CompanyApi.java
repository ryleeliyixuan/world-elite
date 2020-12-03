package com.worldelite.job.api;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.CompanyRecommendType;
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

    @Autowired
    private CompanySalaryService companySalaryService;

    @Autowired
    private CompanyEnvironmentService companyEnvironmentService;

    @Autowired
    private CompanyHonorService companyHonorService;

    @Autowired
    private CompanyDepartmentService companyDepartmentService;

    @Autowired
    private CompanyRecruitService companyRecruitService;

    @Autowired
    private CompanyRecommendService companyRecommendService;

    @Autowired
    private WikiModuleService wikiModuleService;

    @Autowired
    private CompanyNameSearchService companyNameSearchService;
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
     * 获取公司信息
     * @param id 公司ID
     * @return
     */
    @GetMapping("company-base-data")
    public ApiResult<CompanyVo> getCompanyBaseData(@RequestParam Long id){
        CompanyVo companyVo = companyService.getSimpleCompanyInfo(id);
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
     * 获取百科模块启用情况
     * @param listForm
     * @return
     */
    @ApiDoc
    @PostMapping("list-wiki-module")
    public ApiResult<PageResult<CompanyWikiVo>> listWikiModule(@RequestBody CompanyWikiListForm listForm){
        PageResult<CompanyWikiVo> pageResult = companyWikiService.listSimple(listForm);
        return ApiResult.ok(pageResult);
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
     * @param wikiModuleForm 百科表单
     * @return
     */
    @ApiDoc
    @PostMapping("change-module-enable")
    public ApiResult<CompanyWikiVo> changeModuleEnable(@RequestBody WikiModuleForm wikiModuleForm){
        WikiModule wikiModule = wikiModuleService.save(wikiModuleForm);
        return ApiResult.ok(new WikiModuleVo().asVo(wikiModule));
    }

    /**
     * 新增百科薪资
     * @param companySalaryForm 薪资表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-salary")
    public ApiResult<CompanySalaryVo> saveSalary(@RequestBody CompanySalaryForm companySalaryForm){
        CompanySalary salary = companySalaryService.save(companySalaryForm);
        return ApiResult.ok(companySalaryService.toVo(salary));
    }

    /**
     * 删除百科薪资
     * @param salaryId 薪资ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-salary")
    public ApiResult deleteSalary(@RequestParam Integer salaryId){
        companySalaryService.deleteById(salaryId);
        return ApiResult.ok();
    }

    /**
     * 查询公司薪资
     * @param companyId 公司薪资ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-salary")
    public ApiResult<List<CompanySalaryVo>> listSalary(@RequestParam Long companyId){
        List<CompanySalaryVo> salaryVoList = companySalaryService.listVoByCompanyId(companyId);
        return ApiResult.ok(salaryVoList);
    }

    /**
     * 新增百科荣誉
     * @param companyHonorForm 荣誉表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-honor")
    public ApiResult<CompanyHonorVo> saveHonor(@RequestBody CompanyHonorForm companyHonorForm){
        CompanyHonor honor = companyHonorService.save(companyHonorForm);
        return ApiResult.ok(companyHonorService.toVo(honor));
    }

    /**
     * 删除百科荣誉
     * @param honorId 荣誉ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-honor")
    public ApiResult deleteHonor(@RequestParam Integer honorId){
        companyHonorService.deleteById(honorId);
        return ApiResult.ok();
    }

    /**
     * 查询公司荣誉
     * @param companyId 公司荣誉ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-honor")
    public ApiResult<List<CompanyHonorVo>> listHonor(@RequestParam Long companyId){
        List<CompanyHonorVo> honorVoList = companyHonorService.listVoByCompanyId(companyId);
        return ApiResult.ok(honorVoList);
    }


    /**
     * 新增百科招聘时间线
     * @param companyRecruitForm 招聘时间线表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-recruit")
    public ApiResult<CompanyRecruitVo> saveRecruit(@RequestBody CompanyRecruitForm companyRecruitForm){
        CompanyRecruit recruit = companyRecruitService.save(companyRecruitForm);
        return ApiResult.ok(companyRecruitService.toVo(recruit));
    }

    /**
     * 删除百科招聘时间线
     * @param recruitId 招聘时间线ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-recruit")
    public ApiResult deleteRecruit(@RequestParam Integer recruitId){
        companyRecruitService.deleteById(recruitId);
        return ApiResult.ok();
    }

    /**
     * 查询公司招聘时间线
     * @param companyId 公司招聘时间线ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-recruit")
    public ApiResult<List<CompanyRecruitVo>> listRecruit(@RequestParam Long companyId){
        List<CompanyRecruitVo> recruitVoList = companyRecruitService.listVoByCompanyId(companyId);
        return ApiResult.ok(recruitVoList);
    }


    /**
     * 新增百科环境
     * @param companyEnvironmentForm 环境表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-environment")
    public ApiResult<CompanyEnvironmentVo> saveEnvironment(@RequestBody CompanyEnvironmentForm companyEnvironmentForm){
        CompanyEnvironment environment = companyEnvironmentService.save(companyEnvironmentForm);
        return ApiResult.ok(companyEnvironmentService.toVo(environment));
    }

    /**
     * 删除百科环境
     * @param environmentId 环境ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-environment")
    public ApiResult deleteEnvironment(@RequestParam Integer environmentId){
        companyEnvironmentService.deleteById(environmentId);
        return ApiResult.ok();
    }

    /**
     * 查询公司环境
     * @param companyId 公司环境ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-environment")
    public ApiResult<List<CompanyEnvironmentVo>> listEnvironment(@RequestParam Long companyId){
        List<CompanyEnvironmentVo> environmentVoList = companyEnvironmentService.listVoByCompanyId(companyId);
        return ApiResult.ok(environmentVoList);
    }

    /**
     * 新增百科部门
     * @param companyDepartmentForm 部门表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-company-department")
    public ApiResult<CompanyDepartmentVo> saveDepartment(@RequestBody CompanyDepartmentForm companyDepartmentForm){
        CompanyDepartment department = companyDepartmentService.save(companyDepartmentForm);
        return ApiResult.ok(companyDepartmentService.toVo(department));
    }

    /**
     * 删除百科部门
     * @param departmentId 部门ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-company-department")
    public ApiResult deleteDepartment(@RequestParam Integer departmentId){
        companyDepartmentService.deleteById(departmentId);
        return ApiResult.ok();
    }

    /**
     * 查询公司部门
     * @param companyId 公司部门ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-company-department")
    public ApiResult<List<CompanyDepartmentVo>> listDepartment(@RequestParam Long companyId){
        List<CompanyDepartmentVo> departmentVoList = companyDepartmentService.listVoByCompanyId(companyId);
        return ApiResult.ok(departmentVoList);
    }

    /**
     * 新增百科推荐帖子
     * @param companyRecommendForm 推荐帖子表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-recommend-post")
    public ApiResult<CompanyRecommendVo> savePost(@RequestBody CompanyRecommendForm companyRecommendForm){
        companyRecommendForm.setObjectType(CompanyRecommendType.POST.value);
        CompanyRecommend recommend = companyRecommendService.save(companyRecommendForm);
        return ApiResult.ok(companyRecommendService.toVo(recommend));
    }

    /**
     * 删除百科推荐帖子
     * @param recommendId 推荐帖子ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-recommend-post")
    public ApiResult deletePost(@RequestParam Integer recommendId){
        companyRecommendService.deleteById(recommendId);
        return ApiResult.ok();
    }

    /**
     * 查询公司推荐帖子
     * @param companyId 公司推荐帖子ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-recommend-post")
    public ApiResult<List<CompanyRecommendVo>> listPost(@RequestParam Long companyId){
        List<CompanyRecommendVo> recommendVoList = companyRecommendService.listVoByCompanyId(companyId);
        return ApiResult.ok(recommendVoList);
    }

    /**
     * 新增百科推荐职位
     * @param companyRecommendForm 推荐职位表单
     * @return
     */
    @ApiDoc
    @PostMapping("save-recommend-job")
    public ApiResult<CompanyRecommendVo> saveJob(@RequestBody CompanyRecommendForm companyRecommendForm){
        companyRecommendForm.setObjectType(CompanyRecommendType.JOB.value);
        CompanyRecommend recommend = companyRecommendService.save(companyRecommendForm);
        return ApiResult.ok(companyRecommendService.toVo(recommend));
    }

    /**
     * 删除百科推荐职位
     * @param recommendId 推荐职位ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete-recommend-job")
    public ApiResult deleteJob(@RequestParam Integer recommendId){
        companyRecommendService.deleteById(recommendId);
        return ApiResult.ok();
    }

    /**
     * 查询公司推荐职位
     * @param companyId 公司推荐职位ID
     * @return
     */
    @ApiDoc
    @GetMapping("list-recommend-job")
    public ApiResult<List<CompanyRecommendVo>> listJob(@RequestParam Long companyId){
        List<CompanyRecommendVo> recommendVoList = companyRecommendService.listVoByCompanyId(companyId);
        return ApiResult.ok(recommendVoList);
    }


    /**
     * 创建或刷新职位名索引
     *
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("create-or-refresh-company-name-index")
    public ApiResult createOrRefreshJobNameIndex() {
        companyNameSearchService.createOrRefreshJobNameIndex();
        return ApiResult.ok();
    }


    /**
     * 根据名字搜索job
     *
     * @param searchNameForm
     * @return
     */
    @ApiDoc
    @GetMapping("search-company-name")
    public ApiResult<PageResult<String>> searchJobName(@RequestBody SearchNameForm searchNameForm) {
        companyNameSearchService.createOrRefreshJobNameIndex();
        PageResult<String> result = companyNameSearchService.searchJobName(searchNameForm);
        return ApiResult.ok(result);
    }

}
