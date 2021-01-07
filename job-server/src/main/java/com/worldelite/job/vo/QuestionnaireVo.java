package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.Questionnaire;
import lombok.Data;

import java.util.List;

/**
 * 问题视图
 */
@Data
public class QuestionnaireVo implements VoConvertable<QuestionnaireVo, Questionnaire>{

    private Integer id; //问卷ID
    private String title; //问题标题
    private String type; //问题类型
    private String mustAnswer; //是否必答
    private Integer sort; //排序
    private List<QuestionnaireOptionsVo> optionsList; //选项列表

    @Override
    public QuestionnaireVo asVo(Questionnaire questionnaire) {
        BeanUtil.copyProperties(questionnaire,this);
        return this;
    }
}
