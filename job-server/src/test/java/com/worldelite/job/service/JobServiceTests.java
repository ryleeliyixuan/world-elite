package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.service.sched.ScheduleService;
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
public class JobServiceTests {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void test_takeOffJobs(){
        scheduleService.takeOffExpiredJobs();
    }

}
