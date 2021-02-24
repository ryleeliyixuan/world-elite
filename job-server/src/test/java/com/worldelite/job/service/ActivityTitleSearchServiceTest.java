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
 * Nuo Xu
 */
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class ActivityTitleSearchServiceTest {

    @Autowired
    private ActivitySearchService activityTitleSearchService;

    @Test
    public void testCreateOrRefreshActivityTitleIndex() {
//        activityTitleSearchService.createOrRefreshActivityTitleIndex();
    }


    @Test
    public void testSearchActivityTitle() {
//        activityTitleSearchService.createOrRefreshActivityTitleIndex();

        PageResult<String> result = activityTitleSearchService.searchActivityTitle("北京", 1, 5);
        System.out.println("result = " + result);
    }

}
