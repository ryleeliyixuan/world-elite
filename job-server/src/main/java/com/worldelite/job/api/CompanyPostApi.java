package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.form.CompanyCommentForm;
import com.worldelite.job.form.CompanyPostForm;
import com.worldelite.job.form.CompanyPostListForm;
import com.worldelite.job.form.CompanyReportForm;
import com.worldelite.job.service.CompanyPostService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CompanyCommentVo;
import com.worldelite.job.vo.CompanyPostVo;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 企业帖子接口
 */
@Validated
@RestController
@RequestMapping("/api/company-post/")
public class CompanyPostApi extends BaseApi {

    @Autowired
    private CompanyPostService companyPostService;

    /**
     * 保存帖子数据
     * @param form 帖子表单
     * @return 保存后的帖子
     */
    @ApiDoc
    @PostMapping("save")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyPostVo> save(@RequestBody CompanyPostForm form){
        CompanyPost companyPost = companyPostService.save(form);
        return ApiResult.ok(companyPostService.getPostVo(companyPost));
    }

    /**
     * 删除帖子
     * @param postId 帖子ID
     */
    @ApiDoc
    @PostMapping("delete")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult delete(@RequestParam Long postId){
        companyPostService.delete(postId);
        return ApiResult.ok();
    }

    /**
     * 点赞
     * @param postId 帖子ID
     */
    @ApiDoc
    @PostMapping("like")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyPostVo> like(@RequestParam Long postId){
        CompanyPostVo companyPostVo = companyPostService.like(postId);
        return ApiResult.ok(companyPostVo);
    }

    /**
     * 举报
     * @param companyReportForm 举报表单
     * @return
     */
    @ApiDoc
    @PostMapping("report")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult<CompanyPostVo> report(@RequestBody CompanyReportForm companyReportForm){
        CompanyPostVo companyPostVo = companyPostService.report(companyReportForm);
        return ApiResult.ok(companyPostVo);
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
        CompanyCommentVo commentVo = companyPostService.comment(companyCommentForm);
        return ApiResult.ok(commentVo);
    }

    /**
     * 查询帖子
     * @param listForm 查询表单
     * @return 帖子列表
     */
    @ApiDoc
    @PostMapping("list")
    public ApiResult<PageResult<CompanyPostVo>> list(@RequestBody CompanyPostListForm listForm){
        PageResult<CompanyPostVo> companyPostPageResult = companyPostService.listVo(listForm);
        return ApiResult.ok(companyPostPageResult);
    }

    /**
     * 获取帖子详情
     * @param postId 帖子ID
     * @return
     */
    @ApiDoc
    @GetMapping("detail")
    public ApiResult getDetail(@RequestParam Long postId){
        CompanyPostVo postVo = companyPostService.getPostVo(postId);
        return ApiResult.ok(postVo);
    }
}
