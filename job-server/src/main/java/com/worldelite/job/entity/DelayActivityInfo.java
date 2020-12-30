package com.worldelite.job.entity;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class DelayActivityInfo implements Delayed {
    private Integer activityId;
    private long expire;

    public DelayActivityInfo(Integer activityId, long expire) {
        this.activityId = activityId;
        setExpire(expire);
    }

    public void setExpire(long expire) {
        this.expire = System.currentTimeMillis() + expire;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
        long delta = getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return (int) delta;
    }
}
