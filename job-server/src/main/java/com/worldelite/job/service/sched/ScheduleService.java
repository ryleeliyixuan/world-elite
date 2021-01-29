package com.worldelite.job.service.sched;

import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.entity.Job;
import com.worldelite.job.entity.JobOptions;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.service.DownloadService;
import com.worldelite.job.service.FileService;
import com.worldelite.job.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@Service
public class ScheduleService {

    @Autowired
    private FileService fileService;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private JobMapper jobMapper;

    @Value("${job.expired.days}")
    private Integer jobExpiredDays;

    @Autowired
    private JobService jobService;

    /**
     * 定时清理缓存，每2个小时清理一次
     */
    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000)
    public void clearLocalCache() {
        log.debug("清理本机缓存开始");
        Date before = DateUtils.addHours(new Date(), -2);
        downloadService.clearDownloadRecords(before);
        fileService.clearCacheFiles(before);
        log.debug("清理本机缓存结束");
    }

    /**
     * 下架过期职位，每天凌晨触发
     */
    //@Scheduled(cron = "0 0 0 * * *")
    public void takeOffExpiredJobs() {
        JobOptions jobOptions = new JobOptions();
        jobOptions.setStatus(JobStatus.PUBLISH.value);
        Date endPubDate = DateUtils.addDays(new Date(), -jobExpiredDays);
        Date startPubDate = DateUtils.addDays(endPubDate, -500);
        jobOptions.setStartPubTime(startPubDate);
        jobOptions.setEndPubTime(endPubDate);
        List<Job> jobList = jobMapper.selectAndList(jobOptions);
        for (Job job : jobList) {
            jobService.takeOffJob(true, job.getId(), String.format("发布时间超过%s天，系统自动下架处理", jobExpiredDays));
        }
    }
}
