package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.JobCategory;
import com.worldelite.job.mapper.JobCategoryMapper;
import com.worldelite.job.vo.JobCategoryVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class JobCategoryService {

    @Autowired
    private JobCategoryMapper jobCategoryMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public JobCategoryVo getById(Integer id){
        JobCategory jobCategory = jobCategoryMapper.selectByPrimaryKey(id);
        return jobCategory == null? null: new JobCategoryVo().asVo(jobCategory);
    }

    /**
     * 获取分类树
     *
     * @return
     */
    public List<JobCategoryVo> getCategoryTree(){
        final String categoryTreeJson = redisTemplate.opsForValue().get(RedisKeys.JOB_CATEGORY_TREE);
        if(StringUtils.isNotEmpty(categoryTreeJson)){
            return JSON.parseArray(categoryTreeJson, JobCategoryVo.class);
        }
        List<JobCategoryVo> categoryTree = getCategoryTree(0);
        redisTemplate.opsForValue().set(RedisKeys.JOB_CATEGORY_TREE, JSON.toJSONString(categoryTree), 10, TimeUnit.MINUTES);
        return categoryTree;
    }

    private List<JobCategoryVo> getCategoryTree(Integer parentId){
        JobCategory options = new JobCategory();
        options.setParentId(parentId);
        PageHelper.orderBy("position asc");
        List<JobCategory> categoryList = jobCategoryMapper.selectAndList(options);
        if(CollectionUtils.isEmpty(categoryList)){
            return null;
        }
        List<JobCategoryVo> jobCategoryVoList = new ArrayList<>(categoryList.size());
        for(JobCategory category : categoryList){
            JobCategoryVo categoryVo = new JobCategoryVo().asVo(category);
            categoryVo.setChildren(getCategoryTree(category.getId()));
            jobCategoryVoList.add(categoryVo);
        }
        return jobCategoryVoList;
    }
}
