package com.worldelite.job.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author Xiang Chao
 **/
@ToString
public class ActivityInfoRefreshEvent extends ApplicationEvent {
    /**
     * 活动id, 有值刷新指定活动, null刷新全部
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
    public ActivityInfoRefreshEvent(Object source) {
        super(source);
    }

    public ActivityInfoRefreshEvent(Object source, Integer activityId) {
        super(source);
        this.activityId = activityId;
    }
}
