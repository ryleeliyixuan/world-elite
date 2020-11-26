package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.vo.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @Author twz
 * @Date 2020/11/26 0026
 * @Desc TODO
 */
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class JobNameSearchServiceTest {

    @Autowired
    private JobNameSearchService jobNameSearchService;

    @Test
    public void testCreateOrRefreshJobNameIndex() {
        jobNameSearchService.createOrRefreshJobNameIndex();
    }


    @Test
    public void testSearchJobName() {
        jobNameSearchService.createOrRefreshJobNameIndex();

        PageResult<String> result = jobNameSearchService.searchJobName("研", 1, 5);
        System.out.println("result = " + result);
    }

}
