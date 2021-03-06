package com.worldelite.job.service.activity;

import com.worldelite.job.constants.ActivityStatus;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.context.SpringContextHolder;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.ActivityOptions;
import com.worldelite.job.entity.DelayActivityInfo;
import com.worldelite.job.event.ActivityInfoRefreshEvent;
import com.worldelite.job.mapper.ActivityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

/**
 * 活动状态管理
 *
 * @author Xiang Chao
 **/
@Slf4j
@Component
@AllArgsConstructor
public class ActivityStatusManager implements CommandLineRunner {
    private final ActivityMapper activityMapper;
    private final DelayQueue<DelayActivityInfo> delayQueue = new DelayQueue<>();

    /**
     * 加入到延时队列中
     */
    public void put(DelayActivityInfo delayActivityInfo) {
        delayQueue.add(delayActivityInfo);
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

    public void put(Activity activity) {
        //仅对有效的活动创建索引
        if (activity.getStatus() == ActivityStatus.REVIEWING.value
                || activity.getStatus() == ActivityStatus.REVIEW_FAILURE.value
                || activity.getStatus() == ActivityStatus.DRAFT.value
                || activity.getStatus() == ActivityStatus.OFFLINE.value
                || activity.getDelFlag() == Bool.TRUE) return;

        long diff;
        if (activity.getNeedRegistration() != null && activity.getNeedRegistration() == Bool.TRUE) {
            if (activity.getRegistrationStartTime() != null && (diff = ChronoUnit.MILLIS.between(Instant.now(), activity.getRegistrationStartTime().toInstant())) > 0) {
                put(new DelayActivityInfo(activity.getId(), diff));
            }
            if (activity.getRegistrationFinishTime() != null && (diff = ChronoUnit.MILLIS.between(Instant.now(), activity.getRegistrationFinishTime().toInstant())) > 0) {
                put(new DelayActivityInfo(activity.getId(), diff));
            }
        }
        if (activity.getActivityStartTime() != null && (diff = ChronoUnit.MILLIS.between(Instant.now(), activity.getActivityStartTime().toInstant())) > 0) {
            put(new DelayActivityInfo(activity.getId(), diff));
        }
        if (activity.getActivityFinishTime() != null && (diff = ChronoUnit.MILLIS.between(Instant.now(), activity.getActivityFinishTime().toInstant())) > 0) {
            put(new DelayActivityInfo(activity.getId(), diff));
        }

        //延迟0ms放入队列中,用于刷新当前的活动状态
        put(new DelayActivityInfo(activity.getId(), 0));
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

                log.warn("活动状态延迟队列刷新处理, 活动ID:{}, 活动状态:{}", activityInfo.getActivityId(), activity.getStatus());

                final ActivityStatus activityStatus = getActivityStatus(activity);
                //如果数据库中活动状态与算出来的不一样就要更新
                if (activityStatus.value != activity.getStatus()) {
                    Activity ac = new Activity();
                    ac.setId(activity.getId());
                    ac.setStatus(activityStatus.value);
                    activityMapper.updateByPrimaryKeySelective(ac);
                    SpringContextHolder.publishEvent(new ActivityInfoRefreshEvent(this, activity.getId()));

                    log.warn("活动ID:{}, 活动状态更新为{}", activityInfo.getActivityId(), activityStatus.value);
                }
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
                    || activity.getDelFlag() == Bool.TRUE) return;

            put(activity);
        });
    }

    public ActivityStatus getActivityStatus(Activity activity) {
        Date date = new Date();
        ActivityStatus status;

        Date registrationStartTime, registrationFinishTime, activityStartTime, activityFinishTime;

        registrationStartTime = activity.getRegistrationStartTime() == null ? new Date(0) : activity.getRegistrationStartTime();
        registrationFinishTime = activity.getRegistrationFinishTime() == null ? new Date(0) : activity.getRegistrationFinishTime();
        activityStartTime = activity.getActivityStartTime() == null ? new Date(0) : activity.getActivityStartTime();
        activityFinishTime = activity.getActivityFinishTime() == null ? new Date(0) : activity.getActivityFinishTime();

        //当前时间小于报名开始时间
        if (date.compareTo(registrationStartTime) < 0) {
            status = ActivityStatus.WILL;
        }
        //当前时间大于报名开始时间,小于报名结束时间
        else if (date.compareTo(registrationStartTime) >= 0 && date.compareTo(registrationFinishTime) <= 0) {
            status = ActivityStatus.SIGN_UP;
        }
        //当前时间大于报名结束时间,小于活动开始时间
        else if (date.compareTo(registrationFinishTime) > 0 && date.compareTo(activityStartTime) < 0) {
            status = ActivityStatus.WILL;
        }
        //当前时间大于活动开始时间,小于活动结束时间
        else if (date.compareTo(activityStartTime) >= 0 && date.compareTo(activityFinishTime) <= 0) {
            status = ActivityStatus.ACTIVE;
        }
        //活动结束
        else {
            status = ActivityStatus.END;
        }

        return status;
    }
}
