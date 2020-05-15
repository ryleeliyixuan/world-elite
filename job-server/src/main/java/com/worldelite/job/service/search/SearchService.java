package com.worldelite.job.service.search;

import com.worldelite.job.form.JobSearchForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeVo;

/**
 * 搜索接口
 *
 * @author yeguozhong yedaxia.github.com
 */
public interface SearchService {

    /**
     * 搜索工作
     * @param searchForm
     * @return
     */
    PageResult<JobVo> searchJob(JobSearchForm searchForm);

    /**
     * 给某个工作推荐职位
     *
     * @param jobId
     * @return
     */
    PageResult<ResumeVo> getJobRecommendResumes(Long jobId, PageForm pageForm);
}
