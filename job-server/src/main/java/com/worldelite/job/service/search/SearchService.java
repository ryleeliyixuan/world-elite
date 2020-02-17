package com.worldelite.job.service.search;

import com.worldelite.job.form.JobSearchForm;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;

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

}
