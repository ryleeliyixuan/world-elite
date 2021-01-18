package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.RecommendForm;
import com.worldelite.job.form.RecommendListForm;
import com.worldelite.job.service.RecommendService;
import com.worldelite.job.service.SubscribeService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.RecommendVo;
import com.worldelite.job.vo.SearchJobHistoryVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订阅接口
 */
@RestController
@RequestMapping("/api/subscribe/")
public class SubscribeApi {

    @Autowired
    private SubscribeService subscribeService;

    /**
     * 订阅职位
     * @param jobId
     * @return
     */
    @ApiDoc
    @PostMapping("job")
    @RequireLogin
    public ApiResult addJob(Long jobId){
        subscribeService.subscribeJob(jobId);
        return ApiResult.ok();
    }

    /**
     * 从订阅职位生成搜索记录
     * @return
     */
    @ApiDoc
    @GetMapping("search-job-history")
    @RequireLogin
    public ApiResult<SearchJobHistoryVo> subscribeSearchJobHistory(){
        SearchJobHistoryVo searchJobHistoryVo = subscribeService.getMySearchJobHistory();
        return ApiResult.ok(searchJobHistoryVo);
    }

}
