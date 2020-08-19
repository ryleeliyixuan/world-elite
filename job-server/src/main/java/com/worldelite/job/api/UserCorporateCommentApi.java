package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.UserCorporateCommentForm;
import com.worldelite.job.service.UserCorporateCommentService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.UserCorporateCommentVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户笔记操作接口
 *
 * @author 王星瀚
 */
@RestController
@RequestMapping("/api/usercorporate/comment/")
public class UserCorporateCommentApi {

    @Autowired
    private UserCorporateCommentService userCorporateCommentService;

    /**
     * 保存用户笔记
     * @param userCorporateCommentForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveComment(@RequestBody UserCorporateCommentForm userCorporateCommentForm){
        userCorporateCommentService.saveComment(userCorporateCommentForm);
        return ApiResult.ok();
    }

    /**
     * 获取某用户的所有笔记
     *
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<List<UserCorporateCommentVo>> getCommentList(@RequestParam long userId, @RequestParam long corporateId) {
        final List<UserCorporateCommentVo> userCorporateCommentVos = userCorporateCommentService
                .getCommentsByUserIdAndCompanyId(userId, corporateId);

        return ApiResult.ok(userCorporateCommentVos);
    }

    /**
     * 删除用户笔记
     *
     * @param userCorporateCommentId
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteComment(@RequestBody long userCorporateCommentId){
        userCorporateCommentService.deleteCommentById(userCorporateCommentId);

        return ApiResult.ok();
    }
}
