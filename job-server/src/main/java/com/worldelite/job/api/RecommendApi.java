package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.form.RecommendForm;
import com.worldelite.job.form.RecommendListForm;
import com.worldelite.job.service.RecommendService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CompanyVo;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.RecommendVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 推荐接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/recommend/")
public class RecommendApi {

    @Autowired
    private RecommendService recommendService;

    /**
     * 保存推荐
     * @param recommendForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveRecommend(@RequestBody RecommendForm recommendForm){
        recommendService.saveRecommend(recommendForm);
        return ApiResult.ok();
    }

    /**
     * 删除推荐
     *
     * @param id 推荐ID
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("delete")
    @ApiDoc
    public ApiResult delRecommend(@RequestParam Integer id){
        recommendService.deleteRecommend(id);
        return ApiResult.ok();
    }

    /**
     * 获取推荐列表
     *
     * @param listForm
     * @return
     */
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<RecommendVo>> getRecommendList(RecommendListForm listForm){
        PageResult pageResult = recommendService.getRecommendList(listForm);
        return ApiResult.ok(pageResult);
    }

    @GetMapping("recent-job-company")
    @ApiDoc
    public ApiResult<PageResult<CompanyVo>> getRecentJobCompany(PageForm pageForm){
        PageResult<CompanyVo> pageResult = recommendService.getRecentJobCompany(pageForm);
        return ApiResult.ok(pageResult);
    }
}
