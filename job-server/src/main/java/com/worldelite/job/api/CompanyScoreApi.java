package com.worldelite.job.api;

import com.worldelite.job.entity.CompanyScore;
import com.worldelite.job.form.CompanyScoreForm;
import com.worldelite.job.form.CompanyScoreListForm;
import com.worldelite.job.service.CompanyScoreService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业评分接口
 */
@RestController
@RequestMapping("/api/company-score/")
@Validated
public class CompanyScoreApi extends BaseApi {

    @Autowired
    private CompanyScoreService companyScoreService;

    /**
     * 保存评分数据
     * @param form 评分表单
     * @return 保存后的评分
     */
    @ApiDoc
    @PostMapping("save")
    public ApiResult<CompanyScore> save(CompanyScoreForm form){
        CompanyScore companyScore = companyScoreService.save(form);
        return ApiResult.ok(companyScore);
    }

    /**
     * 查询评分
     * @param listForm 查询表单
     * @return 评分列表
     */
    @ApiDoc
    @PostMapping("list")
    public ApiResult<PageResult<CompanyScore>> list(CompanyScoreListForm listForm){
        PageResult<CompanyScore> companyScorePageResult = companyScoreService.list(listForm);
        return ApiResult.ok(companyScorePageResult);
    }
}
