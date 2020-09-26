package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.Job;
import lombok.Data;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class JobKeywordsVo implements VoConvertable<JobKeywordsVo, String>{

    private String[] industryTags; //行业领域
    private String[] skillTags; //技能标签

    @Override
    public JobKeywordsVo asVo(String keywords) {
        return this;
    }
}
