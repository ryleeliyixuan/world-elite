package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.JobForm;
import com.worldelite.job.form.JobListForm;
import com.worldelite.job.service.JobService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/job")
public class JobApi extends BaseApi{

    @Autowired
    private JobService jobService;

    /**
     * 保存职位
     *
     * @param jobForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save")
    public ApiResult saveJob(@Valid @RequestBody JobForm jobForm){
        jobService.saveJob(jobForm);
        return ApiResult.ok();
    }

    /**
     * 获取公司已发布职位列表
     *
     * @param jobListForm
     * @return
     */
    @GetMapping("company-job-list")
    public ApiResult getCompanyJobList(JobListForm jobListForm){
        jobListForm.setStatus(JobStatus.PUBLISH.value);
        PageResult pageResult = jobService.getJobList(jobListForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取职位详情
     *
     * @param id
     * @return
     */
    @GetMapping("get-job-info")
    public ApiResult<JobVo> getJobInfo(@RequestParam Long id){
        JobVo jobVo = jobService.getJobInfo(id);
        return ApiResult.ok(jobVo);
    }

    /**
     * 获取用户管理职位列表
     *
     * @param listForm
     * @return
     */
    @GetMapping("manage-job-list")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult getManageJobList(JobListForm listForm){
        listForm.setCreatorId(curUser().getId());
        PageResult pageResult = jobService.getJobList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 下架职位
     *
     * @param id
     * @return
     */
    @PostMapping("take-off-job")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult takeOffJob(@RequestParam Long id){
        jobService.takeOffJob(id);
        return ApiResult.ok();
    }

    /**
     * 投递简历
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("apply-job")
    public ApiResult applyJob(@RequestParam Long id){
        jobService.applyJob(id);
        return ApiResult.ok();
    }
}
