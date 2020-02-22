package com.worldelite.job.api;

import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/search")
public class SearchApi {

    @Autowired
    private IndexService indexService;

    @GetMapping("build-index")
    public ApiResult buildIndex(){
        indexService.createOrRefresh();
        return ApiResult.ok();
    }
}
