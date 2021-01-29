package com.worldelite.job.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
public class DelayActivityInfo implements Delayed {
    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 业务类型
     */
    private Integer type;
    /**
     * 过期时间(毫秒)
     */
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
