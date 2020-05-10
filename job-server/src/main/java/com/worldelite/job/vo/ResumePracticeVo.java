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

    private Integer id;
    @JSONField(format = "yyyy-MM")
    private Date startTime;
    @JSONField(format = "yyyy-MM")
    private Date finishTime;
    private String title;
    private String description;
    private String post;
    private Byte onWork;

    @Override
    public ResumePracticeVo asVo(ResumePractice resumePractice) {
        BeanUtil.copyProperties(resumePractice, this);
        return this;
    }
}
