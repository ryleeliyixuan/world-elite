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
    public ApiResult addActivityQuestionnaire(@RequestBody ActivityQuestionnaireForm form){
        activityQuestionnaireService.addActivityQuestionnaire(form);
        return ApiResult.ok();
    }

    /**
     * 根据ID获取报名表模板
     * @param id 报名表ID
     * @return
     */
    @ApiDoc
    @GetMapping("{id}")
    public ApiResult<QuestionnaireTemplateVo> getActivityQuestionnaire(@PathVariable Integer id){
        QuestionnaireTemplateVo template = activityQuestionnaireService.getActivityQuestionnaire(id);
        return ApiResult.ok(template);
    }

    /**
     * 获取活动报名表
     * @return
     */
    @ApiDoc
    @GetMapping("activity/{activityId}")
    @RequireLogin
    public ApiResult<QuestionnaireTemplateVo> getActivityQuestionnaireByActivityId(@PathVariable Integer activityId){
        QuestionnaireTemplateVo template = activityQuestionnaireService.getActivityQuestionnaireByActivityId(activityId);
        return ApiResult.ok(template);
    }

}
