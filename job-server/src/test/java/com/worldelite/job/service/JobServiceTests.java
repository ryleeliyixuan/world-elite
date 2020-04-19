package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.entity.Job;
import com.worldelite.job.entity.JobOptions;
import com.worldelite.job.form.JobForm;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.service.sched.ScheduleService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class JobServiceTests {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void test_takeOffJobs(){
        scheduleService.takeOffExpiredJobs();
    }

}
