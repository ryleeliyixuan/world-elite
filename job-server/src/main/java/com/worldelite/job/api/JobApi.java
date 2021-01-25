package com.worldelite.job.api;

import com.alibaba.druid.sql.PagerUtils;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.ScanResult;
import com.worldelite.job.form.*;
import com.worldelite.job.service.DictService;
import com.worldelite.job.service.IContentScanner;
import com.worldelite.job.service.JobNameSearchService;
import com.worldelite.job.service.JobService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
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
import java.util.Map;

/**
 * 职位接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/job/")
@Validated
@Slf4j
public class JobApi extends BaseApi {

    @Autowired
    private JobService jobService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private DictService dictService;

    @Autowired
    private IContentScanner contentScanner;

    @Autowired
    private JobNameSearchService jobNameSearchServiceImpl;

    /**
     * 保存职位
     *
     * @param jobForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveJob(@Valid @RequestBody JobForm jobForm) throws Exception {
        //Todo 敏感词检测经常超时，影响用户体验，暂时屏蔽
//        ScanResult scanResult = contentScanner.scanText(jobForm.getDescription());
//        if (scanResult.getCode() != ScanResult.CODE_PASS) {
//            return ApiResult.fail(message("content.scan.fail"));
//        }
        jobService.saveJob(jobForm);
        return ApiResult.ok();
    }


    /**
     * 获取公司已发布职位列表
     *
     * @param jobListForm
     * @return
     */
    @ApiDoc
    @GetMapping("company-job-list")
    public ApiResult<PageResult<JobVo>> getCompanyJobList(JobListForm jobListForm) {
        jobListForm.setStatus(JobStatus.PUBLISH.value);
        PageResult pageResult = jobService.getJobList(jobListForm);
        return ApiResult.ok(pageResult);
    }


    /**
     * 获取指定公司不同招聘类型职位数量
     *
     * @param companyId 公司ID
     * @return 职位数量
     */
    @ApiDoc
    @GetMapping("job-recruit-count")
    public ApiResult<JobRecruitVo> getJobRecruitCount(Long companyId) {
        List<JobRecruitVo> pageResult = jobService.getJobRecruitCount(companyId);
        return ApiResult.ok(pageResult);
    }

    /**
     * 简历筛选-职位
     *
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("user-job-options")
    @ApiDoc
    public ApiResult getUserJobOptions() {
        List<JobVo> jobVoList = jobService.getUserJobOptions();
        return ApiResult.ok(jobVoList);
    }



    /*@GetMapping("job-recruit-count")
    @ApiDoc
    public ApiResult getRecruitCount(@RequestParam Long id) {
        Map<String, Integer> recruitsCount = jobService.getRecruitCount(id);
        return ApiResult.ok(recruitsCount);
    }*/


    /**
     * 获取职位详情
     *
     * @param id 职位ID
     * @return
     */
    @ApiDoc
    @GetMapping("get-job-info")
    public ApiResult<JobVo> getJobInfo(@RequestParam Long id) {
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
    @ApiDoc
    public ApiResult<PageResult<JobVo>> getManageJobList(JobListForm listForm) {
        listForm.setCreatorId(curUser().getId());
        PageResult pageResult = jobService.getJobList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 下架职位
     *
     * @param id     职位ID
     * @param reason 理由
     * @return
     */
    @PostMapping("take-off-job")
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
    @ApiDoc
    public ApiResult takeOffJob(@RequestParam Long id, String reason) {
        jobService.takeOffJob(id, reason);
        return ApiResult.ok();
    }

    /**
     * 删除职位
     *
     * @param id 职位ID
     * @return
     */
    @PostMapping("delete")
    @RequireLogin(allow = {UserType.COMPANY, UserType.ADMIN})
    @ApiDoc
    public ApiResult deleteJob(@RequestParam Long id) {
        jobService.deleteJob(id);
        return ApiResult.ok();
    }

    /**
     * 重新开放职位
     *
     * @param id 职位ID
     * @return
     */
    @RequireLogin(allow = {UserType.COMPANY})
    @PostMapping("open-job")
    @ApiDoc
    public ApiResult reOpenJob(@RequestParam Long id) {
        jobService.openJob(id);
        return ApiResult.ok();
    }

    /**
     * 申请工作
     *
     * @param id 职位ID
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("apply-job")
    @ApiDoc
    public ApiResult applyJob(@RequestParam Long id) {
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
    @ApiDoc
    public ApiResult<PageResult<JobVo>> myApplyJobList(ApplyJobListForm listForm) {
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
    @ApiDoc
    public ApiResult<PageResult<JobVo>> getResumeApplyJobList(ApplyJobListForm listForm) {
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
    public ApiResult<PageResult<JobVo>> searchJobList(@RequestBody JobSearchForm searchForm) {
        if (searchForm.getSalaryRangeId() != null) {
            DictVo salaryRange = dictService.getById(searchForm.getSalaryRangeId());
            if (salaryRange != null) {
                String[] values = salaryRange.getValue().split("-");
                if (values.length == 2) {
                    searchForm.setMinSalary(NumberUtils.toInt(values[0]));
                    searchForm.setMaxSalary(NumberUtils.toInt(values[1]));
                }
            }
        }

        PageResult pageResult;

        if (jobService.isEmptySearch(searchForm)) {
//            pageResult = jobService.getUserRecommendJobList(searchForm);
            JobListForm jobListForm = new JobListForm();
            jobListForm.setStatus(JobStatus.PUBLISH.value);
            pageResult = jobService.getJobList(jobListForm);
        } else {
            pageResult = searchService.searchJob(searchForm);
        }

        return ApiResult.ok(pageResult);
    }


    /**
     * 搜索job，按照salary排序
     * salary是 月薪*月份+年终奖
     *
     * @param searchForm
     * @return
     */
    @PostMapping("search-job-order-by-salary")
    public ApiResult<PageResult<JobVo>> searchJobListOrderBySalary(@RequestBody JobSearchForm searchForm) {
        if (searchForm.getSalaryRangeId() != null) {
            DictVo salaryRange = dictService.getById(searchForm.getSalaryRangeId());
            if (salaryRange != null) {
                String[] values = salaryRange.getValue().split("-");
                if (values.length == 2) {
                    searchForm.setMinSalary(NumberUtils.toInt(values[0]));
                    searchForm.setMaxSalary(NumberUtils.toInt(values[1]));
                }
            }
        }

        PageResult pageResult;

        if (jobService.isEmptySearch(searchForm)) {
//            pageResult = jobService.getUserRecommendJobList(searchForm);
            JobListForm jobListForm = new JobListForm();
            jobListForm.setStatus(JobStatus.PUBLISH.value);
            pageResult = jobService.getJobList(jobListForm);
        } else {
            pageResult = searchService.searchJob(searchForm);
        }
        pageResult = jobService.sortJobListBySalary(pageResult, searchForm.getSalaryAsc());
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
    @ApiDoc
    public ApiResult<PageResult<JobVo>> getJobList(JobListForm listForm) {
        PageResult pageResult = jobService.getJobList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取工作简历推荐
     *
     * @param jobId 职位ID
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @GetMapping("recommend-resumes")
    @ApiDoc
    public ApiResult<PageResult<ResumeVo>> getRecommendResumes(@RequestParam Long jobId) {
        PageResult pageResult = searchService.getJobRecommendResumes(jobId, PageForm.pageOf(1, 5));
        return ApiResult.ok(pageResult.getList());
    }

    /**
     * 推荐简历
     *
     * @param jobId    职位ID
     * @param resumeId 简历ID
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("recommend-resume-for-job")
    @ApiDoc
    public ApiResult<PageResult<ResumeVo>> recommendResumeForJob(@RequestParam Long jobId, @RequestParam Long resumeId) {
        jobService.recommendResumeForJob(jobId, resumeId);
        return ApiResult.ok();
    }


    /**
     * 创建或刷新职位名索引
     *
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("create-or-refresh-job-name-index")
    public ApiResult createOrRefreshJobNameIndex() {
        jobNameSearchServiceImpl.createOrRefreshJobNameIndex();
        return ApiResult.ok();
    }


    /**
     * 根据名字搜索job
     *
     * @param searchNameForm
     * @return
     */
    @ApiDoc
    @GetMapping("search-job-name")
    public ApiResult<PageResult<String>> searchJobName(SearchNameForm searchNameForm) {
        jobNameSearchServiceImpl.createOrRefreshJobNameIndex();
        PageResult<String> result = jobNameSearchServiceImpl.searchJobName(searchNameForm);
        return ApiResult.ok(result);
    }

}
