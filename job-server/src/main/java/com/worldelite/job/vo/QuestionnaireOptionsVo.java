package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.QuestionnaireOptions;
import lombok.Data;

/**
 * 问题选项视图
 */
@Data
public class QuestionnaireOptionsVo implements VoConvertable<QuestionnaireOptionsVo, QuestionnaireOptions>{

    private Integer id; //选项ID
    private String options; //选项标签
    private Integer sort; //选项排序

    @Override
    public QuestionnaireOptionsVo asVo(QuestionnaireOptions questionnaireOptions) {
        if(questionnaireOptions == null){
            return null;
        }
        BeanUtil.copyProperties(questionnaireOptions,this);
        return this;
    }
}
