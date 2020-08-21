package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.JobCategoryIndustryForm;
import com.worldelite.job.form.JobIndustryForm;
import com.worldelite.job.service.JobCategoryIndustryService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.JobIndustryVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 职位行业操作接口
 * @author 熊文剑
 */
@RestController
@RequestMapping("/api/jobindustry/")
public class JobCategoryIndustryApi {

    @Autowired
    private JobCategoryIndustryService jobCategoryIndustryService;

    /**
     * 保存职位技能
     * @param jobIndustryForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveJobIndustry(@RequestBody JobIndustryForm jobIndustryForm){
        jobCategoryIndustryService.saveJobIndustry(jobIndustryForm);

        return ApiResult.ok();
    }

    /**
     * 删除职位技能
     * @param jobIndustryId
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteJobIndustry(@RequestParam long jobIndustryId){
        jobCategoryIndustryService.deleteJobIndustryById(jobIndustryId);

        return ApiResult.ok();
    }

    /**
     * 根据职位类型Id获取对应的职位行业领域
     * @param jobCategoryId
     * @return
     */
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
    @GetMapping("list")
    @ApiDoc
    public ApiResult<List<JobIndustryVo>> getJobIndustryList(@RequestParam long jobCategoryId) {
        final List<JobIndustryVo> jobIndustryVos = jobCategoryIndustryService.getJobIndustryByJobCategoryId(jobCategoryId);

        return ApiResult.ok(jobIndustryVos);
    }

    /**
     * 保存职位行业领域-职位类别映射
     * @param jobCategoryIndustryForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("category/save")
    @ApiDoc
    public ApiResult saveJobCategoryIndustry(@RequestBody JobCategoryIndustryForm jobCategoryIndustryForm){
        jobCategoryIndustryService.saveJobCategoryIndustry(jobCategoryIndustryForm);

        return ApiResult.ok();
    }

    /**
     * 删除职位行业领域-职位类别映射
     * @param jobCategoryIndustryId
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("category/delete")
    @ApiDoc
    public ApiResult deleteJobCategoryIndustry(@RequestParam long jobCategoryIndustryId){
        jobCategoryIndustryService.deleteJobCategoryIndustryById(jobCategoryIndustryId);

        return ApiResult.ok();
    }
}
