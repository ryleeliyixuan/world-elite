package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.Admin;
import com.worldelite.job.entity.SkillTag;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

import java.util.Date;

/**
 * @author 邓集阶
 */
@Data
public class SkillTagVo implements VoConvertable<SkillTagVo, SkillTag>{

    private Integer id; //标签ID
    private DictVo type; //标签类型
    private String name; //标签名

    @Override
    public SkillTagVo asVo(SkillTag skillTag) {
        setId(skillTag.getId());
        setName(skillTag.getName());
        return this;
    }
}
