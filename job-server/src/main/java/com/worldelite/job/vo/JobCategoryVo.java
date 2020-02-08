package com.worldelite.job.vo;

import com.worldelite.job.entity.JobCategory;
import lombok.Data;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class JobCategoryVo implements VoConvertable<JobCategoryVo, JobCategory>{

    private Integer id;
    private String name;
    private Integer position;

    private List<JobCategoryVo> children;

    @Override
    public JobCategoryVo asVo(JobCategory jobCategory) {
        setId(jobCategory.getId());
        setName(jobCategory.getName());
        setPosition(jobCategory.getPosition());
        return this;
    }
}
