package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.ActivityQuestionnaireForm;
import com.worldelite.job.service.ActivityQuestionnaireService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.QuestionnaireTemplateVo;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报名表接口
 */
@Slf4j
@RestController
@RequestMapping("api/activity-questionnaire")
public class ActivityQuestionnaireApi {

    @Autowired
    private ActivityQuestionnaireService activityQuestionnaireService;

    /**
     * 添加报名表
     * @param form
     * @return
     */
    @ApiDoc
    @PostMapping
    @RequireLogin
    public ApiResult<QuestionnaireTemplateVo> addActivityQuestionnaire(@RequestBody ActivityQuestionnaireForm form){
        QuestionnaireTemplateVo activityQuestionnaire = activityQuestionnaireService.addActivityQuestionnaire(form);
        return ApiResult.ok(activityQuestionnaire);
    }

    /**
     * 修改报名表
     * @param id 报名表ID
     * @param form 报名表单
     * @return
     */
    @ApiDoc
    @PatchMapping("{id}")
    @RequireLogin
    public ApiResult<QuestionnaireTemplateVo> updateActivityQuestionnaire(@PathVariable Integer id,@RequestBody ActivityQuestionnaireForm form){
        QuestionnaireTemplateVo activityQuestionnaire = activityQuestionnaireService.updateActivityQuestionnaire(id,form);
        return ApiResult.ok(activityQuestionnaire);
    }

    /**
     * 根据ID获取报名表
     * @param id 报名表ID
     * @return
     */
    @ApiDoc
    @GetMapping("{id}")
    public ApiResult<QuestionnaireTemplateVo> getActivityQuestionnaire(@PathVariable Integer id){
        QuestionnaireTemplateVo template = activityQuestionnaireService.getActivityQuestionnaire(id);
        return ApiResult.ok(template);
    }
}
