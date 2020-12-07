package com.worldelite.job.entity;

import java.util.Date;

/**
 * Nuo Xu
 */
public class ResumeEduOptions extends ResumeEdu{
    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
