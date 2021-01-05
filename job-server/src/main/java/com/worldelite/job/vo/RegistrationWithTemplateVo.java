package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.QuestionnaireTemplate;
import com.worldelite.job.entity.Registration;
import lombok.Data;

import java.util.List;

/**
 * 报名表模板与活动报名视图
 */
@Data
public class RegistrationWithTemplateVo{
    private QuestionnaireTemplateVo template; //报名表模板
    private RegistrationVo registration; //报名表信息
}
