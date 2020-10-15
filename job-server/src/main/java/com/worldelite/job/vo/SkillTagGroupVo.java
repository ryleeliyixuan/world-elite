package com.worldelite.job.vo;

import com.worldelite.job.entity.SkillTag;
import lombok.Data;

import java.util.List;

/**
 * @author 邓集阶
 */
@Data
public class SkillTagGroupVo{
    private DictVo type; //标签类型
    private List<SkillTagVo> skillList; //标签名
}
