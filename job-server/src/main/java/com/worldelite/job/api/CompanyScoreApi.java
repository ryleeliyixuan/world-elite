package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.CompanyScore;
import com.worldelite.job.form.CompanyCommentForm;
import com.worldelite.job.form.CompanyReportForm;
import com.worldelite.job.form.CompanyScoreForm;
import com.worldelite.job.form.CompanyScoreListForm;
import com.worldelite.job.service.CompanyScoreService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CompanyCommentVo;
import com.worldelite.job.vo.CompanyScoreVo;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyScoreVo> save(@RequestBody CompanyScoreForm form){
        CompanyScore companyScore = companyScoreService.save(form);
        return ApiResult.ok(companyScoreService.getScoreVo(companyScore));
    }

    /**
     * 删除评分
     * @param scoreId 评分ID
     */
    @ApiDoc
    @PostMapping("delete")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult delete(@RequestParam Long scoreId){
        companyScoreService.delete(scoreId);
        return ApiResult.ok();
    }

    /**
     * 点赞
     * @param scoreId 评分ID
     */
    @ApiDoc
    @PostMapping("like")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyScoreVo> like(@RequestParam Long scoreId){
        CompanyScoreVo companyScoreVo = companyScoreService.like(scoreId);
        return ApiResult.ok(companyScoreVo);
    }

    /**
     * 举报
     * @param companyReportForm 举报表单
     * @return
     */
    @ApiDoc
    @PostMapping("report")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyScoreVo> report(@RequestBody CompanyReportForm companyReportForm){
        CompanyScoreVo companyScoreVo = companyScoreService.report(companyReportForm);
        return ApiResult.ok(companyScoreVo);
    }

    /**
     * 评论
     * @param companyCommentForm 评论表单
     * @return
     */
    @ApiDoc
    @PostMapping("comment")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyCommentVo> comment(@RequestBody CompanyCommentForm companyCommentForm){
        CompanyCommentVo commentVo = companyScoreService.comment(companyCommentForm);
        return ApiResult.ok(commentVo);
    }

    /**
     * 查询评分
     * @param listForm 查询表单
     * @return 评分列表
     */
    @ApiDoc
    @PostMapping("list")
    public ApiResult<PageResult<CompanyScoreVo>> list(@RequestBody CompanyScoreListForm listForm){
        PageResult<CompanyScoreVo> companyScorePageResult = companyScoreService.listVo(listForm);
        return ApiResult.ok(companyScorePageResult);
    }
}
