package com.worldelite.job.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 活动下架事件
 *
 * @author Xiang Chao
 **/
public class ActivityTakeOffEvent extends ApplicationEvent {
    /**
     * 活动id
     */
    @Getter
    @Setter
    private Integer activityId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ActivityTakeOffEvent(Object source, Integer activityId) {
        super(source);
        this.activityId = activityId;
    }
}
