package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.JobForm;
import com.worldelite.job.form.JobListForm;
import com.worldelite.job.form.JobSearchForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.DictService;
import com.worldelite.job.service.JobService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.DictVo;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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

    @Autowired
    private SearchService searchService;

    @Autowired
    private DictService dictService;

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
        JobVo jobVo = jobService.getJobDetail(id);
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
     * 申请工作
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

    /**
     * 获取当前用户申请的职位列表
     *
     * @return
     */
    @GetMapping("my-apply-jobs")
    @RequireLogin(allow = UserType.GENERAL)
    public ApiResult myApplyJobList(PageForm pageForm){
        PageResult<JobVo> pageResult = jobService.getUserApplyJobList(pageForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 搜索职位
     *
     * @param searchForm
     * @return
     */
    @PostMapping("search-job")
    public ApiResult searchJobList(@RequestBody JobSearchForm searchForm){
        if(searchForm.getSalaryRangeId() != null){
            DictVo salaryRange =  dictService.getById(searchForm.getSalaryRangeId());
            if(salaryRange != null){
                String[] values =  salaryRange.getValue().split("-");
                if(values.length == 2){
                    searchForm.setMinSalary(NumberUtils.toInt(values[0]));
                    searchForm.setMaxSalary(NumberUtils.toInt(values[1]));
                }
            }
        }

        PageResult pageResult;
        if(StringUtils.isEmpty(searchForm.getKeyword())){
            pageResult = jobService.getUserRecommendJobList(searchForm);
        }else{
            pageResult =  searchService.searchJob(searchForm);
        }

        return ApiResult.ok(pageResult);
    }
}
