package com.worldelite.job.api;

import com.worldelite.job.service.*;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.JobKeywordsVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 职位类型接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/category/")
public class JobCategoryApi extends BaseApi{

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private AdditionIndustryService additionIndustryService;

    @Autowired
    private AdditionSkillService additionSkillService;

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
        List<JobCategoryVo> jobCategoryVoList = jobCategoryService.getCacheTree();
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
        String[] additionIndustryTags = additionIndustryService.listByName(curUser().getId(),name);
        String[] additionSkillTags = additionSkillService.listByName(categoryId.longValue(),curUser().getId(),name);
        JobKeywordsVo jobKeywordsVo = new JobKeywordsVo();
        jobKeywordsVo.setSkillTags(mergeStrArray(skillTags,additionSkillTags));
        jobKeywordsVo.setIndustryTags(mergeStrArray(industryTags,additionIndustryTags));
        return ApiResult.ok(jobKeywordsVo);
    }

    private String[] mergeStrArray(String[] array1,String[] array2){
        int l1 = array1.length;
        int l2 = array2.length;
        array1 = Arrays.copyOf(array1, l1+l2);//数组扩容
        System.arraycopy(array2, 0, array1, l1, l2);
        return array1;
    }
}
