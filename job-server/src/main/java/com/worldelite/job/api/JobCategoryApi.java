package com.worldelite.job.api;

import com.worldelite.job.service.JobCategoryService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.JobCategoryVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 职位类型接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/category/")
public class JobCategoryApi {

    @Autowired
    private JobCategoryService jobCategoryService;

    /**
     * 获取职位类型树
     * @return
     */
    @GetMapping("category-tree")
    @ApiDoc
    public ApiResult<List<JobCategoryVo>> categoryTree(){
        List<JobCategoryVo> jobCategoryVoList = jobCategoryService.getCategoryTree();
        return ApiResult.ok(jobCategoryVoList);
    }
}
