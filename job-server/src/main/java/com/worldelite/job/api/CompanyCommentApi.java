package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.*;
import com.worldelite.job.service.CompanyCommentService;
import com.worldelite.job.vo.*;
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
     * @param deleteForm 评论删除表单
     */
    @ApiDoc
    @PostMapping("delete")
    @RequireLogin
    public ApiResult delete(@RequestBody CompanyCommentDeleteForm deleteForm){
        companyCommentService.delete(deleteForm.getCommentId());
        return ApiResult.ok();
    }

    /**
     * 批量删除评论
     * @param deleteForm
     * @return
     */
    @ApiDoc
    @PostMapping("delete-all")
    @RequireLogin
    public ApiResult deleteAll(@RequestBody CompanyCommentDeleteForm deleteForm){
        companyCommentService.deleteAll(deleteForm.getCommentIds());
        return ApiResult.ok();
    }

    /**
     * 点赞
     * @param commentId 评论ID
     */
    @ApiDoc
    @PostMapping("like")
    @RequireLogin
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
    @RequireLogin
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
    @RequireLogin
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

    /**
     * 查询帖子的评论
     * @param listForm
     * @return
     */
    @ApiDoc
    @PostMapping("search-in-post")
    public ApiResult<PageResult<PostCommentVo>> searchInPost(@RequestBody CompanyCommentListForm listForm){
        PageResult<PostCommentVo> pageResult = companyCommentService.searchInPost(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 查询评分的评论
     * @param listForm
     * @return
     */
    @ApiDoc
    @PostMapping("search-in-score")
    public ApiResult<PageResult<ScoreCommentVo>> searchInScore(@RequestBody CompanyCommentListForm listForm){
        PageResult<ScoreCommentVo> pageResult = companyCommentService.searchInScore(listForm);
        return ApiResult.ok(pageResult);
    }
}
