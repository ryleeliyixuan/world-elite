package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.JobCategory;
import com.worldelite.job.mapper.JobCategoryMapper;
import com.worldelite.job.vo.JobCategoryVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public JobCategoryVo getById(Integer id) {
        JobCategory jobCategory = jobCategoryMapper.selectByPrimaryKey(id);
        return jobCategory == null ? null : new JobCategoryVo().asVo(jobCategory);
    }

    public JobCategory getCategory(Integer id) {
        return jobCategoryMapper.selectByPrimaryKey(id);
    }


    /**
     * 从缓存中获取分类树
     *
     * @return 分类树
     */
    public List<JobCategoryVo> getCacheTree() {
        //从Redis中取缓存数据
        final String categoryTreeJson = redisTemplate.opsForValue().get(RedisKeys.JOB_CATEGORY_TREE);
        if (StringUtils.isNotEmpty(categoryTreeJson)) {
            return JSON.parseArray(categoryTreeJson, JobCategoryVo.class);
        }
        //没有缓存则新建缓存
        List<JobCategoryVo> categoryTree = getCategoryTree();
        redisTemplate.opsForValue().set(RedisKeys.JOB_CATEGORY_TREE, JSON.toJSONString(categoryTree), 10, TimeUnit.MINUTES);
        return categoryTree;
    }


    /**
     * 获取分类树
     *
     * @return 分类树
     */
    public List<JobCategoryVo> getCategoryTree() {
        //从数据库取出所有数据
        List<JobCategory> jobCategoryList = jobCategoryMapper.selectAndList(new JobCategory());
        //将城市数据保存到Map以便生成树形结构时能直接取到父类
        Map<Integer, JobCategoryVo> jobCategoryMap = new HashMap<>();
        for (JobCategory jobCategory : jobCategoryList) {
            jobCategoryMap.put(jobCategory.getId(), new JobCategoryVo().asVo(jobCategory));
        }
        //生成城市树列表
        List<JobCategoryVo> rootList = new ArrayList<>();
        for (Integer jobCategoryId : jobCategoryMap.keySet()) {
            JobCategoryVo jobCategoryVo = jobCategoryMap.get(jobCategoryId);
            if (jobCategoryVo.getParentId() == null || jobCategoryVo.getParentId() == 0) {
                //防止数据异常，再将父节点ID统一设为0
                jobCategoryVo.setParentId(0);
                rootList.add(jobCategoryVo);
            } else {
                //添加子节点
                JobCategoryVo parentJobCategory = jobCategoryMap.get(jobCategoryVo.getParentId());
                if (parentJobCategory.getChildren() == null) {
                    parentJobCategory.setChildren(new ArrayList<>());
                }
                parentJobCategory.getChildren().add(jobCategoryVo);
            }
        }
        return rootList;
    }

}
