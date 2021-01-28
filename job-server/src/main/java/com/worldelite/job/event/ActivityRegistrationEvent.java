package com.worldelite.job.event;

import com.worldelite.job.constants.RegistrationStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 活动报名审核事件
 *
 * @author Xiang Chao
 **/
public class ActivityRegistrationEvent extends ApplicationEvent {
    /**
     * 报名id
     */
    @Getter
    @Setter
    private Integer registrationId;

    /**
     * 活动报名审核状态, {@link RegistrationStatus}
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
    public ActivityRegistrationEvent(Object source, Integer registrationId, Byte status) {
        super(source);
        this.registrationId = registrationId;
        this.status = status;
    }
}
