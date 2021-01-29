package com.worldelite.job.service.activity;

import com.worldelite.job.constants.ActivityStatus;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.DelayActivityInfo;

import java.util.Date;
import java.util.concurrent.DelayQueue;

/**
 * @author Xiang Chao
 **/
public abstract class AbstractActivityManager {
    protected final DelayQueue<DelayActivityInfo> delayQueue = new DelayQueue<>();

    /**
     * 加入到延时队列中
     */
    public void put(DelayActivityInfo delayActivityInfo) {
        delayQueue.put(delayActivityInfo);
    }

    public void put(Activity activity) {
        put(activity, null);
    }

    public abstract void put(Activity activity, Integer type);

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

    public ActivityStatus getActivityStatus(Activity activity) {
        Date date = new Date();
        ActivityStatus status;
        if (date.compareTo(activity.getRegistrationStartTime()) < 0) {
            status = ActivityStatus.WILL;
        } else if (date.compareTo(activity.getRegistrationStartTime()) >= 0 && date.compareTo(activity.getRegistrationFinishTime()) <= 0) {
            status = ActivityStatus.SIGN_UP;
        } else if (date.compareTo(activity.getRegistrationFinishTime()) > 0 && date.compareTo(activity.getActivityStartTime()) < 0) {
            status = ActivityStatus.WILL;
        } else if (date.compareTo(activity.getActivityStartTime()) >= 0 && date.compareTo(activity.getActivityFinishTime()) <= 0) {
            status = ActivityStatus.ACTIVE;
        } else {
            status = ActivityStatus.END;
        }

        return status;
    }
}
