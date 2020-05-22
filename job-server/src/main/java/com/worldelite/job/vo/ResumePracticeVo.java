package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ResumePractice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumePracticeVo extends EditFlag implements VoConvertable<ResumePracticeVo, ResumePractice>{

    private Integer id; //实践ID
    @JSONField(format = "yyyy-MM")
    private Date startTime; //开始时间
    @JSONField(format = "yyyy-MM")
    private Date finishTime; //结束时间
    private String title; //标题
    private String description; //描述
    private String post; //职位
    private Byte onWork; //是否实践中

    @Override
    public ResumePracticeVo asVo(ResumePractice resumePractice) {
        BeanUtil.copyProperties(resumePractice, this);
        return this;
    }
}
