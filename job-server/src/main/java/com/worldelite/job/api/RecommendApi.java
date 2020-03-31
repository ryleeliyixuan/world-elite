package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.RecommendForm;
import com.worldelite.job.form.RecommendListForm;
import com.worldelite.job.service.RecommendService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/recommend")
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
    public ApiResult saveRecommend(@RequestBody RecommendForm recommendForm){
        recommendService.saveRecommend(recommendForm);
        return ApiResult.ok();
    }

    /**
     * 删除推荐
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("delete")
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
    public ApiResult getRecommendList(RecommendListForm listForm){
        PageResult pageResult = recommendService.getRecommendList(listForm);
        return ApiResult.ok(pageResult);
    }
}
