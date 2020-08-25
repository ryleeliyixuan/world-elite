package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.form.UserCorporateTagForm;
import com.worldelite.job.service.UserCorporateTagService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.UserCorporateTagVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业标签操作接口
 *
 * @author 熊文剑
 */
@RestController
@RequestMapping("/api/usercorporate/tag/")
public class UserCorporateTagApi extends BaseController {

    @Autowired
    private UserCorporateTagService userCorporateTagService;

    /**
     * 保存用户标签
     * @param userCorporateTagForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveTag(@RequestBody UserCorporateTagForm userCorporateTagForm){
        userCorporateTagService.saveTag(userCorporateTagForm);
        return ApiResult.ok();
    }

    /**
     * 获取标签列表
     *
     * @param jobApplyId
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<List<UserCorporateTagVo>> getTagList(@RequestParam long jobApplyId) {
        final List<UserCorporateTagVo> userCorporateTagVos = userCorporateTagService.getTagsByJobApplyId(jobApplyId);
        return ApiResult.ok(userCorporateTagVos);
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteTag(@RequestParam Long id) {
        userCorporateTagService.delTag(id);
        return ApiResult.ok();
    }
}
