package com.worldelite.job.service;

import com.worldelite.job.entity.JobDescExample;
import com.worldelite.job.form.JobDescExampleForm;
import com.worldelite.job.mapper.JobDescExampleMapper;
import com.worldelite.job.vo.JobDescExampleVo;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JobDescExampleService {
    @Autowired
    JobDescExampleMapper jobDescExampleMapper;

    /**
     * 写入新的职位描述示例
     * @param jobDescExampleForm
     * @return
     */
    public void saveExample(final @NonNull JobDescExampleForm jobDescExampleForm) {
        final JobDescExample jobDescExample = new JobDescExample();

        if(StringUtils.isNotEmpty(jobDescExampleForm.getDescription())) {
            jobDescExample.setDescription(jobDescExampleForm.getDescription());
        }
        jobDescExample.setId(jobDescExampleForm.getId());
        jobDescExample.setCategoryId(jobDescExampleForm.getCategoryId());

        jobDescExampleMapper.insertSelective(jobDescExample);
    }

    /**
     * 根据职位类型id取得职位描述示例
     * @param categoryId
     * @return
     */
    public List<JobDescExampleVo> getExamplesByCategoryId(final @NonNull Long categoryId) {
        final List<JobDescExample> jobDescExamples = jobDescExampleMapper.selectByCategoryId(categoryId);

        if (Objects.nonNull(jobDescExamples)) {
            return jobDescExamples.stream()
                    .map(jobDescExample -> new JobDescExampleVo().asVo(jobDescExample))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * 删除职位描述示例
     *
     * @param id
     */
    public void delExample(Long id){
        JobDescExample example = jobDescExampleMapper.selectByPrimaryKey(id);
        if(example != null){
            jobDescExampleMapper.deleteByPrimaryKey(id);
        }
    }
}
