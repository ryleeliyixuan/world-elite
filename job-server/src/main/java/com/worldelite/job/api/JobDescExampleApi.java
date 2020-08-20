package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.JobDescExampleForm;
import com.worldelite.job.service.JobDescExampleService;
import com.worldelite.job.entity.JobDescExample;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.JobDescExampleVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 职位描述操作接口
 * @author 熊文剑
 */
@RestController
@RequestMapping("/api/jobdescexample/")
public class JobDescExampleApi {

    @Autowired
    private JobDescExampleService jobDescExampleService;

    /**
     * 保存职位描述示例
     * @param jobDescExampleForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveExample(@RequestBody JobDescExampleForm jobDescExampleForm){
        jobDescExampleService.saveExample(jobDescExampleForm);
        return ApiResult.ok();
    }

    /**
     * 获取所有的职位描述示例
     *
     * @param categoryId
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<List<JobDescExampleVo>> getExampleList(@RequestParam long categoryId){
        final List<JobDescExampleVo> jobDescExampleVos = jobDescExampleService
                .getExamplesByCategoryId(categoryId);

        return ApiResult.ok(jobDescExampleVos);
    }

    /**
     * 删除职位描述示例
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("del")
    @ApiDoc
    public ApiResult deleteExample(@RequestBody Long id) {
        jobDescExampleService.delExample(id);
        return ApiResult.ok();
    }
}
