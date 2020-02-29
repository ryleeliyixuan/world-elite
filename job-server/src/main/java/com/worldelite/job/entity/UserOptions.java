package com.worldelite.job.entity;

import com.worldelite.job.util.TimeUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserOptions extends User{

    private Date beginTime;
    private Date endTime;
    private Long companyId;
    private Byte verifyStatus;

    public void setEndTime(Date endTime) {
        this.endTime = TimeUtils.finalSecOfTheDay(endTime);
    }
}
