package com.worldelite.job.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchJobTipsVo implements VoConvertable<SearchJobTipsVo, Object> {

    //职位名称提示
    private List<JobVo> jobList;
    //职位类型提示
    private List<JobCategoryVo> jobCategoryVoList;

    @Override
    public SearchJobTipsVo asVo(Object s) {
        return null;
    }

    /**
     * 重写asVo方法
     * 使用两个参数实例化职位智能搜索Vo
     * @param jobList
     * @param jobCategoryVoList
     * @return
     */
    public SearchJobTipsVo asVo(List<JobVo> jobList,List<JobCategoryVo> jobCategoryVoList){
        setJobList(jobList);
        setJobCategoryVoList(jobCategoryVoList);
        return this;
    }
}
