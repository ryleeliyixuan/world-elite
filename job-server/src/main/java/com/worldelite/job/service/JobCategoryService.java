package com.worldelite.job.service;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.JobCategory;
import com.worldelite.job.mapper.JobCategoryMapper;
import com.worldelite.job.vo.JobCategoryVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class JobCategoryService {

    @Autowired
    private JobCategoryMapper jobCategoryMapper;

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
        return getCategoryTree(0);
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
