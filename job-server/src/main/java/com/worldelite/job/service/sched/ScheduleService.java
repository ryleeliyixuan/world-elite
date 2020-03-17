package com.worldelite.job.service.sched;

import com.worldelite.job.service.DownloadService;
import com.worldelite.job.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

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


    /**
     * 定时清理缓存，每2个小时清理一次
     */
    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000)
    public void clearLocalCache(){
        log.debug("清理本机缓存开始");
        Date before = DateUtils.addHours(new Date(), -2);
        downloadService.clearDownloadRecords(before);
        fileService.clearCacheFiles(before);
        log.debug("清理本机缓存结束");
    }
}
