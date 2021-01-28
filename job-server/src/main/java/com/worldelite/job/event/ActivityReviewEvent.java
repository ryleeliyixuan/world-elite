package com.worldelite.job.event;

import com.worldelite.job.constants.VerificationStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 活动审核事件
 *
 * @author Xiang Chao
 **/
public class ActivityReviewEvent extends ApplicationEvent {
    /**
     * 活动id
     */
    @Getter
    @Setter
    private Integer activityId;

    /**
     * 活动审核状态, {@link VerificationStatus}
     */
    @Getter
    @Setter
    private Byte status;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ActivityReviewEvent(Object source, Integer activityId, Byte status) {
        super(source);
        this.activityId = activityId;
        this.status = status;
    }
}
