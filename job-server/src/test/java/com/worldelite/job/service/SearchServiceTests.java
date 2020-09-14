package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.form.JobSearchForm;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author yeguozhong yedaxia.github.com
 */
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class SearchServiceTests {

    @Autowired
    private IndexService indexService;

    @Autowired
    private SearchService searchService;

    @Test
    public void test_buildIndex(){
        indexService.createOrRefresh();
    }

    @Test
    public void test_search(){
        JobSearchForm searchForm = new JobSearchForm();
        searchForm.setKeyword("客服");
        PageResult<JobVo> pageResult = searchService.searchJob(searchForm);
        System.out.println(pageResult.getTotal());
    }
}
