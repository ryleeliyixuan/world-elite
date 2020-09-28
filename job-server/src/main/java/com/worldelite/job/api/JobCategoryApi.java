package com.worldelite.job.api;

import com.worldelite.job.service.JobCategoryIndustryService;
import com.worldelite.job.service.JobCategoryService;
import com.worldelite.job.service.JobCategorySkillService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.JobKeywordsVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 职位类型接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/category/")
public class JobCategoryApi {

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private JobCategorySkillService jobCategorySkillService;

    @Autowired
    private JobCategoryIndustryService jobCategoryIndustryService;

    /**
     * 获取职位类型树
     * @return
     */
    @GetMapping("category-tree")
    @ApiDoc
    public ApiResult<List<JobCategoryVo>> categoryTree(){
        List<JobCategoryVo> jobCategoryVoList = jobCategoryService.getCategoryTree();
        return ApiResult.ok(jobCategoryVoList);
    }

    /**
     * 根据标签名搜索标签
     * @param categoryId 职位类型ID
     * @param name 标签名
     * @return
     */
    @ApiDoc
    @GetMapping("tag-keywords")
    public ApiResult<JobKeywordsVo> getKeywords(@RequestParam Integer categoryId,@RequestParam String name){
        String[] skillTags = jobCategorySkillService.getJobSkillByName(categoryId,name);
        String[] industryTags = jobCategoryIndustryService.getJobIndustryByName(name);
        JobKeywordsVo jobKeywordsVo = new JobKeywordsVo();
        jobKeywordsVo.setSkillTags(skillTags);
        jobKeywordsVo.setIndustryTags(industryTags);
        return ApiResult.ok(jobKeywordsVo);
    }
}
