package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.CompanyCommentForm;
import com.worldelite.job.form.CompanyCommentListForm;
import com.worldelite.job.form.CompanyReportForm;
import com.worldelite.job.service.CompanyCommentService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CompanyCommentVo;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 企业评论接口
 */
@RestController
@RequestMapping("/api/company-comment/")
@Validated
public class CompanyCommentApi extends BaseApi {

    @Autowired
    private CompanyCommentService companyCommentService;

    /**
     * 删除评论
     * @param commentId 评论ID
     */
    @ApiDoc
    @PostMapping("delete")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult delete(@RequestParam Long commentId){
        companyCommentService.delete(commentId);
        return ApiResult.ok();
    }

    /**
     * 点赞
     * @param commentId 评论ID
     */
    @ApiDoc
    @PostMapping("like")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyCommentVo> like(@RequestParam Long commentId){
        CompanyCommentVo companyCommentVo = companyCommentService.like(commentId);
        return ApiResult.ok(companyCommentVo);
    }

    /**
     * 举报
     * @param companyReportForm 举报表单
     * @return
     */
    @ApiDoc
    @PostMapping("report")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyCommentVo> report(@RequestBody CompanyReportForm companyReportForm){
        CompanyCommentVo companyCommentVo = companyCommentService.report(companyReportForm);
        return ApiResult.ok(companyCommentVo);
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
        CompanyCommentVo commentVo = companyCommentService.comment(companyCommentForm);
        return ApiResult.ok(commentVo);
    }

    /**
     * 查询评论
     * @param listForm 查询表单
     * @return 评论列表
     */
    @ApiDoc
    @PostMapping("list")
    public ApiResult<PageResult<CompanyCommentVo>> list(@RequestBody CompanyCommentListForm listForm){
        PageResult<CompanyCommentVo> companyCommentPageResult = companyCommentService.listVo(listForm);
        return ApiResult.ok(companyCommentPageResult);
    }
}
