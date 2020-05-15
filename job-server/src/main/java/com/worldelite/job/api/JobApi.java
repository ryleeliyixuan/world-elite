package com.worldelite.job.api;

import com.alibaba.druid.sql.PagerUtils;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.ScanResult;
import com.worldelite.job.form.*;
import com.worldelite.job.service.DictService;
import com.worldelite.job.service.IContentScanner;
import com.worldelite.job.service.JobService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.DictVo;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/job")
@Validated
@Slf4j
public class JobApi extends BaseApi{

    @Autowired
    private JobService jobService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private DictService dictService;

    @Autowired
    private IContentScanner contentScanner;

    /**
     * 保存职位
     *
     * @param jobForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save")
    public ApiResult saveJob(@Valid @RequestBody JobForm jobForm) throws Exception{
        ScanResult scanResult = contentScanner.scanText(jobForm.getDescription());
        if(scanResult.getCode() != ScanResult.CODE_PASS){
            return ApiResult.fail(message("content.scan.fail"));
        }
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
     * 简历筛选-职位
     *
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("user-job-options")
    public ApiResult getUserJobOptions(){
        List<JobVo> jobVoList = jobService.getUserJobOptions();
        return ApiResult.ok(jobVoList);
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
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
    public ApiResult takeOffJob(@RequestParam Long id, String reason){
        jobService.takeOffJob(id, reason);
        return ApiResult.ok();
    }

    /**
     * 删除职位
     *
     * @param id
     * @return
     */
    @PostMapping("delete")
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
    public ApiResult deleteJob(@RequestParam Long id){
        jobService.deleteJob(id);
        return ApiResult.ok();
    }

    /**
     * 重新开放职位
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = {UserType.COMPANY})
    @PostMapping("open-job")
    public ApiResult reOpenJob(@RequestParam Long id){
        jobService.openJob(id);
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
    public ApiResult myApplyJobList(ApplyJobListForm listForm){
        listForm.setUserId(curUser().getId());
        PageResult<JobVo> pageResult = jobService.getApplyJobList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取简历投递的岗位
     *
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("resume-apply-jobs")
    public ApiResult getResumeApplyJobList(ApplyJobListForm listForm){
        PageResult<JobVo> pageResult = jobService.getApplyJobList(listForm);
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

    /**
     * 获取工作列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("list")
    public ApiResult getJobList(JobListForm listForm){
        PageResult pageResult = jobService.getJobList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取工作简历推荐
     *
     * @param jobId
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("recommend-resumes")
    public ApiResult getRecommendResumes(@RequestParam Long jobId){
        PageResult pageResult = searchService.getJobRecommendResumes(jobId, PageForm.pageOf(1, 5));
        return ApiResult.ok(pageResult.getList());
    }
}
