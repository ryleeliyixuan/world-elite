package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.worldelite.job.entity.QuestionnaireTemplate;
import lombok.Data;

import java.util.List;

/**
 * 报名表模板视图
 */
@Data
public class QuestionnaireTemplateVo implements VoConvertable<QuestionnaireTemplateVo, QuestionnaireTemplate>{

    private Integer id; //模板ID
    private Integer activityId; //活动ID
    private String userId; //用户ID
    private String templateName; //模板名
    private String title; //报名表标题
    private String nameFlag; //姓名标志
    private String genderFlag; //性别标志
    private String phoneFlag; //手机号标志
    private String emailFlag; //邮箱标志
    private String schoolFlag; //学校标志
    private String gradeFlag; //年级标志
    private String professionFlag; //专业标志
    private String educationFlag; //学历标志
    private List<QuestionnaireVo> questionnaireList; //问题列表

    @Override
    public QuestionnaireTemplateVo asVo(QuestionnaireTemplate questionnaireTemplate) {
        BeanUtil.copyProperties(questionnaireTemplate,this);
        setUserId(String.valueOf(questionnaireTemplate.getUserId()));
        return this;
    }
}
