package com.worldelite.job.service.sched;

import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.entity.*;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.mapper.FavoriteMapper;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.service.*;
import com.worldelite.job.vo.UserApplicantVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageResource messageSource;

    @Autowired
    private UserApplicantService userApplicantService;

    @Autowired
    private IEmailService emailService;

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

    /**
     * 每天中午12点扫描活动关注列表 通知用户第二天将要开始报名的活动
     */
    @Scheduled(cron = "0 0 12 * * *")
    public void notifyActivityRegistration() {
        Favorite options = new Favorite();
        options.setType(FavoriteType.ACTIVITY.value);
        final List<Favorite> favorites = favoriteMapper.selectAndList(options);
        favorites.forEach(favorite -> {
            if (favorite.getObjectId() == null) return;

            final Activity activity = activityMapper.selectByPrimaryKey(Math.toIntExact(favorite.getObjectId()));
            if (activity == null || activity.getRegistrationStartTime() == null) return;

            Calendar calendar = new Calendar.Builder().setInstant(activity.getRegistrationStartTime().getTime()).build();
            Calendar utcNow = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

            //活动报名的前一天发送通知
            if (calendar.get(Calendar.YEAR) == utcNow.get(Calendar.YEAR)
                    && calendar.get(Calendar.DAY_OF_YEAR) - utcNow.get(Calendar.DAY_OF_YEAR) == 1) {

                final String msg = messageSource.getMessage("activity.start.notify", activity.getTitle());

                Message message = new Message();
                message.setToUser(favorite.getUserId());
                message.setContent(msg);
                message.setUrl(String.format("/activity/%s", activity.getId()));
                message.setMsgType((byte) 1);
                messageService.sendMessage(message);

                //邮件通知
                UserApplicantVo user = userApplicantService.getUserInfo(favorite.getUserId());
                if (user != null) {
                    String email = user.getEmail();
                    if (StringUtils.isNotEmpty(email)) {
                        EmailForm emailForm = new EmailForm();
                        emailForm.setAddress(email);
                        emailForm.setSubject(msg);
                        emailForm.setEmailBody(msg);
                        emailService.sendEmail(emailForm);
                    }
                }
            }
        });
    }
}
