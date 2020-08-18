package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.UserApplicantCommentForm;
import com.worldelite.job.service.UserApplicantCommentService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.UserApplicantCommentVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户笔记操作接口
 * @author 王星瀚
 */
@RestController
@RequestMapping("/api/userapplicant-comment/")
public class UserApplicantCommentApi {

    @Autowired
    private UserApplicantCommentService userApplicantCommentService;

    /**
     * 保存用户笔记
     * @param userApplicantCommentForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save-comment")
    @ApiDoc
    public ApiResult saveComment(@RequestBody UserApplicantCommentForm userApplicantCommentForm){
        userApplicantCommentService.saveComment(userApplicantCommentForm);
        return ApiResult.ok();
    }

    /**
     * 获取某用户的所有笔记
     *
     * @param userApplicantCommentForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("list-user-comments")
    @ApiDoc
    public ApiResult<List<UserApplicantCommentVo>> getCommentList(
            @RequestBody UserApplicantCommentForm userApplicantCommentForm){
        final List<UserApplicantCommentVo> userApplicantCommentVos = userApplicantCommentService
                .getCommentsByUserIdAndCompanyId(userApplicantCommentForm);

        return ApiResult.ok(userApplicantCommentVos);
    }

    /**
     * 删除用户笔记
     *
     * @param userApplicantCommentId
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("delete-comment")
    @ApiDoc
    public ApiResult deleteComment(@RequestBody long userApplicantCommentId){
        userApplicantCommentService.deleteCommentById(userApplicantCommentId);

        return ApiResult.ok();
    }
}
