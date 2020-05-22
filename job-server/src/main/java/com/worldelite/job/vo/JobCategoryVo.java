package com.worldelite.job.vo;

import com.worldelite.job.entity.JobCategory;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class JobCategoryVo implements VoConvertable<JobCategoryVo, JobCategory>{

    private Integer id; //类型ID
    private String name; //类型名
    private Integer parentId; //父类型
    private Integer position; //排序

    @Ignore
    private List<JobCategoryVo> children;

    @Override
    public JobCategoryVo asVo(JobCategory jobCategory) {
        setId(jobCategory.getId());
        setName(jobCategory.getName());
        setParentId(jobCategory.getParentId());
        setPosition(jobCategory.getPosition());
        return this;
    }
}
