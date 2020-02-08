package com.worldelite.job.api;

import com.worldelite.job.service.JobCategoryService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.JobCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/category")
public class JobCategoryApi {

    @Autowired
    private JobCategoryService jobCategoryService;

    @GetMapping("category-tree")
    public ApiResult categoryTree(){
        List<JobCategoryVo> jobCategoryVoList = jobCategoryService.getCategoryTree();
        return ApiResult.ok(jobCategoryVoList);
    }
}
