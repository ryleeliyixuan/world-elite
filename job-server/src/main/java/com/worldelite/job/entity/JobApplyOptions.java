package com.worldelite.job.entity;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class JobApplyOptions extends JobApply{

    private Long creatorId;
    private String jobIds;
    private String name;

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getJobIds() {
        return jobIds;
    }

    public void setJobIds(String jobIds) {
        this.jobIds = jobIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
