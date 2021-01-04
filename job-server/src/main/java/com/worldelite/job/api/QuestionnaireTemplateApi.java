package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.entity.Questionnaire;
import com.worldelite.job.entity.QuestionnaireTemplate;
import com.worldelite.job.form.QuestionnaireTemplateForm;
import com.worldelite.job.service.QuestionnaireTemplateService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.QuestionnaireTemplateVo;
import com.worldelite.job.vo.QuestionnaireVo;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 报名表模板接口
 */
@Slf4j
@RestController
@RequestMapping("api/questionnaire-template")
public class QuestionnaireTemplateApi {

    @Autowired
    private QuestionnaireTemplateService questionnaireTemplateService;

    /**
     * 添加报名表模板
     * @param form
     * @return
     */
    @ApiDoc
    @PostMapping
    public ApiResult addQuestionnaireTemplate(@RequestBody QuestionnaireTemplateForm form){
        questionnaireTemplateService.addQuestionnaireTemplate(form);
        return ApiResult.ok();
    }

    /**
     * 根据ID获取报名表模板
     * @param id 模板ID
     * @return
     */
    @ApiDoc
    @GetMapping("{id}")
    public ApiResult<QuestionnaireTemplateVo> getQuestionnaireTemplate(@PathVariable Integer id){
        QuestionnaireTemplateVo template = questionnaireTemplateService.getQuestionnaireTemplate(id);
        return ApiResult.ok(template);
    }

    /**
     * 获取我的报名表列表
     * @return
     */
    @ApiDoc
    @GetMapping("my/list")
    @RequireLogin
    public ApiResult<List<QuestionnaireTemplateVo>> getMyQuestionnaireTemplateList(){
        List<QuestionnaireTemplateVo> templateList = questionnaireTemplateService.getMyQuestionnaireTemplateList();
        return ApiResult.ok(templateList);
    }

}
