package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.QuestionnaireOptions;
import com.worldelite.job.form.QuestionnaireOptionsForm;
import com.worldelite.job.mapper.QuestionnaireOptionsMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.QuestionnaireOptionsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import java.util.List;

@Slf4j
@Service
public class QuestionnaireOptionsService extends BaseService {

    @Autowired
    private QuestionnaireOptionsMapper questionnaireOptionsMapper;

    /**
     * 添加问卷选项
     * @param questionnaireOptionsForm
     */
    public void addQuestionnaireOptions(QuestionnaireOptionsForm questionnaireOptionsForm){
        QuestionnaireOptions options = new QuestionnaireOptions();
        BeanUtil.copyProperties(questionnaireOptionsForm,options);
        questionnaireOptionsMapper.insertSelective(options);
    }

    public List<QuestionnaireOptionsVo> getQuestionnaireOptionsList(Integer questionnaireId){
        QuestionnaireOptions options = new QuestionnaireOptions();
        options.setQuestionnaireId(questionnaireId);
        List<QuestionnaireOptions> optionsList = questionnaireOptionsMapper.selectAndList(options);
        return AppUtils.asVoList(optionsList,QuestionnaireOptionsVo.class);
    }
}
