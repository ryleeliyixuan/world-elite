package com.worldelite.job.service.activity;

import com.alibaba.excel.util.DateUtils;
import com.worldelite.job.constants.ActivityStatus;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.entity.*;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.mapper.FavoriteMapper;
import com.worldelite.job.mapper.RegistrationMapper;
import com.worldelite.job.service.*;
import com.worldelite.job.vo.UserApplicantVo;
import com.worldelite.job.vo.UserCorporateVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

/**
 * 活动通知管理器
 *
 * @author Xiang Chao
 **/
@Slf4j
@Component
@AllArgsConstructor
public class ActivityNotifyManager implements CommandLineRunner {
    private final ActivityMapper activityMapper;
    private final RegistrationMapper registrationMapper;
    private final FavoriteMapper favoriteMapper;
    private final MessageService messageService;
    private final MessageResource messageSource;
    private final UserApplicantService userApplicantService;
    private final UserCorporateService userCorporateService;
    private final IEmailService emailService;
    private final CityService cityService;
    private final DelayQueue<DelayActivityInfo> delayQueue = new DelayQueue<>();

    public enum BusinessType {
        /**
         * 活动已报名,开始前一天通知
         */
        ACTIVITY_START(0),
        /**
         * 活动关注未报名, 开始报名时通知
         */
        ACTIVITY_REGISTRATION_START(1);

        public final int value;

        BusinessType(int value) {
            this.value = value;
        }
    }

    /**
     * 加入到延时队列中
     */
    public void put(DelayActivityInfo delayActivityInfo) {
        delayQueue.put(delayActivityInfo);
    }

    /**
     * 取消延时任务
     */
    public boolean remove(DelayActivityInfo delayActivityInfo) {
        return delayQueue.remove(delayActivityInfo);
    }

    /**
     * 取消延时任务
     */
    public boolean remove(Integer activityId) {
        return delayQueue.removeIf(delayActivityInfo -> delayActivityInfo.getActivityId().equals(activityId));
    }

    public void put(Activity activity, Integer type) {
        //仅对有效的活动创建索引
        if (activity.getStatus() == ActivityStatus.REVIEWING.value
                || activity.getStatus() == ActivityStatus.DRAFT.value
                || activity.getStatus() == ActivityStatus.OFFLINE.value
                || activity.getStatus() == ActivityStatus.END.value
                || activity.getDelFlag() == Bool.TRUE) return;

        if (type == null) return;

        long diff;

        if (type == BusinessType.ACTIVITY_START.value) {
            Calendar calendarStart = new Calendar.Builder().setInstant(activity.getActivityStartTime().getTime()).build();
            calendarStart.add(Calendar.DAY_OF_MONTH, -1);

            diff = ChronoUnit.MILLIS.between(Instant.now(), calendarStart.toInstant());
            if (diff > 0)
                put(new DelayActivityInfo(activity.getId(), type, diff));
        }
        if (type == BusinessType.ACTIVITY_REGISTRATION_START.value) {
            diff = ChronoUnit.MILLIS.between(Instant.now(), activity.getRegistrationStartTime().toInstant());
            if (diff > 0)
                put(new DelayActivityInfo(activity.getId(), type, diff));
        }
    }

    @Override
    public void run(String... args) {
        Executors.newSingleThreadExecutor().execute(new Thread(this::executeThread));
    }

    /**
     * 延时任务执行线程
     */
    private void executeThread() {

        activityLoader();

        while (true) {
            try {
                DelayActivityInfo activityInfo = delayQueue.take();

                final Activity activity = activityMapper.selectByPrimaryKey(activityInfo.getActivityId());
                if (activity == null) continue;

                log.warn("活动通知延迟队列处理, 活动ID:{}, 业务类型:{}", activityInfo.getActivityId(), activityInfo.getType());

                if (activityInfo.getType() == BusinessType.ACTIVITY_START.value)
                    activityStartNotifyRegistration(activity);

                if (activityInfo.getType() == BusinessType.ACTIVITY_REGISTRATION_START.value)
                    activityStartNotifyFollower(activity);

            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                break;
            }
        }
    }

    private void activityLoader() {
        ActivityOptions options = new ActivityOptions();
        options.setDelFlag(Bool.FALSE);
        final List<Activity> activities = activityMapper.selectAndList(options);
        if (activities == null || activities.size() == 0) return;

        activities.forEach(activity -> {
            //仅对有效的活动创建索引
            if (activity.getStatus() == ActivityStatus.REVIEWING.value
                    || activity.getStatus() == ActivityStatus.DRAFT.value
                    || activity.getStatus() == ActivityStatus.OFFLINE.value
                    || activity.getStatus() == ActivityStatus.END.value
                    || activity.getDelFlag() == Bool.TRUE) return;

            if (!activityStartNotifyRegistration(activity)) {
                put(activity, BusinessType.ACTIVITY_START.value);
            }
            if (!activityStartNotifyFollower(activity)) {
                put(activity, BusinessType.ACTIVITY_REGISTRATION_START.value);
            }
        });
    }

    /**
     * 活动已报名,开始前一天通知
     *
     * @param activity 活动
     * @return true满足条件发送通知, false不满足条件没发送,需要加入延迟队列
     */
    private boolean activityStartNotifyRegistration(Activity activity) {
        if (activity == null || activity.getActivityStartTime() == null) return false;

        Instant activityStart = activity.getActivityStartTime().toInstant();
        Instant utcNow = Instant.now();
        //活动报名的前一天发送通知
        if (ChronoUnit.MINUTES.between(utcNow, activityStart) > 0 && ChronoUnit.MINUTES.between(utcNow, activityStart) <= 1440) {

            City city = null;
            if (activity.getCityId() != null) {
                city = cityService.getById(activity.getCityId());
            }
            final String toRegistrationMsg = messageSource.getMessage("activity.start.notify", activity.getTitle(), DateUtils.format(activity.getActivityStartTime()), city == null ? "" : city.getName() + activity.getAddress());

            //通知活动报名者
            RegistrationOptions registrationOptions = new RegistrationOptions();
            registrationOptions.setActivityId(activity.getId());
            final List<Registration> registrations = registrationMapper.selectAndList(registrationOptions);

            registrations.forEach(registration -> {
                if (registration.getRegistrationUserId() == null) return;

                sendMsg(registration.getRegistrationUserId(), toRegistrationMsg, String.format("/activity/%s", activity.getId()), 1);
            });

            return true;
        }

        return false;
    }

    /**
     * 活动关注未报名, 开始报名时通知
     *
     * @param activity 活动
     * @return true满足条件发送通知, false不满足条件没发送,需要加入延迟队列
     */
    private boolean activityStartNotifyFollower(Activity activity) {
        if (activity == null || activity.getRegistrationStartTime() == null) return false;

        Calendar calendarStart = new Calendar.Builder().setInstant(activity.getRegistrationStartTime().getTime()).build();
        Calendar calendarFinish = new Calendar.Builder().setInstant(activity.getRegistrationFinishTime().getTime()).build();

        Calendar utcNow = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        //活动报名开始-结束时间内发送通过
        if (calendarStart.after(utcNow) && utcNow.before(calendarFinish)) {
            Favorite options = new Favorite();
            options.setType(FavoriteType.ACTIVITY.value);
            options.setObjectId(Long.valueOf(activity.getId()));
            final List<Favorite> favorites = favoriteMapper.selectAndList(options);
            favorites.forEach(favorite -> {
                //查询关注活动的用户是否已经报名,如果已经报名则跳过发送报名开始通知
                final Registration registration = registrationMapper.selectRegistrationStatusByUserId(activity.getId(), favorite.getUserId());
                if (registration != null) return;

                final String toFollowerMsg = messageSource.getMessage("activity.registration.start.notify", activity.getTitle());
                sendMsg(favorite.getUserId(), toFollowerMsg, String.format("/activity/%s", activity.getId()), 1);
            });

            return true;
        }
        return false;
    }


    private void sendMsg(Long userId, String msg, String url, int type) {
        Message message = new Message();
        message.setToUser(userId);
        message.setContent(msg);
        message.setUrl(url);
        message.setMsgType((byte) type);

        if (messageService.checkNotifyIsSent(message)) {
            log.warn("检测到活动通知消息重复发送,已取消. {}", message);
            return;
        }

        messageService.sendMessage(message);

        //邮件通知
        String email;
        UserApplicantVo applicantVo = userApplicantService.getUserInfo(userId);
        if (applicantVo != null) {
            email = applicantVo.getEmail();
        }else{
            final UserCorporateVo corporateVo = userCorporateService.getUserInfo(userId);
            email = corporateVo != null ? corporateVo.getEmail() : null;
        }

        if (StringUtils.isNotEmpty(email)) {
            EmailForm emailForm = new EmailForm();
            emailForm.setAddress(email);
            emailForm.setSubject(msg);
            emailForm.setEmailBody(msg);
            emailService.sendEmail(emailForm);
        }else{
            log.error("无法获取到用户的邮箱,邮件未发送");
        }
    }
}
