package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.vo.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @Author twz
 * @Date 2020/11/27 0027
 * @Desc TODO
 */
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class CompanyNameSearchServiceTest {

    @Autowired
    private CompanyNameSearchService companyNameSearchService;

    @Test
    public void testCreateOrRefreshJobNameIndex() {
        companyNameSearchService.createOrRefreshJobNameIndex();
    }


    @Test
    public void testSearchJobName() {
        companyNameSearchService.createOrRefreshJobNameIndex();

        PageResult<String> result = companyNameSearchService.searchJobName("北京", 1, 5);
        System.out.println("result = " + result);
    }

}
