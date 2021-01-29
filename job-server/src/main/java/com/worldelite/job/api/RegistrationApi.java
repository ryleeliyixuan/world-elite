package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.RegistrationForm;
import com.worldelite.job.form.RegistrationListForm;
import com.worldelite.job.service.RegistrationService;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 活动报名接口
 */
@RestController
@Slf4j
@RequestMapping("api/registration")
public class RegistrationApi {

    @Autowired
    private RegistrationService registrationService;

    /**
     * 添加活动报名信息
     * @param registrationForm
     * @return
     */
    @ApiDoc
    @RequireLogin
    @PostMapping
    public ApiResult addRegistration(@RequestBody RegistrationForm registrationForm){
        registrationService.addRegistration(registrationForm);
        return ApiResult.ok();
    }

    /**
     * 查询活动报名信息
     * @param registrationListForm
     * @return
     */
    @ApiDoc
    @PostMapping("list")
    public ApiResult<PageResult<RegistrationVo>> list(@RequestBody RegistrationListForm registrationListForm){
        PageResult<RegistrationVo> pageResult = registrationService.getRegistrationList(registrationListForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 根据ID获取报名详情
     * @param id 报名ID
     * @return
     */
    @ApiDoc
    @GetMapping("{id}")
    public ApiResult<RegistrationVo> getRegistration(@PathVariable Integer id){
        QuestionnaireTemplateWithAnswerVo registration = registrationService.getRegistrationWithTemplateDetail(id);
        return ApiResult.ok(registration);
    }

    /**
     * 通过活动报名
     * @param id 报名ID
     * @return
     */
    @ApiDoc
    @PatchMapping("pass/{id}")
    public ApiResult setRegistrationPass(@PathVariable Integer id){
        registrationService.setRegistrationPass(id);
        return ApiResult.ok();
    }

    /**
     * 活动报名不合适
     * @param id 报名ID
     * @return
     */
    @ApiDoc
    @PatchMapping("inappropriate/{id}")
    public ApiResult setRegistrationInappropriate(@PathVariable Integer id){
        registrationService.setRegistrationInappropriate(id);
        return ApiResult.ok();
    }

    /**
     * 当前用户报名活动列表
     *
     * @param pageForm 活动查询表单
     * @return
     */
    @RequireLogin
    @GetMapping("my-registration-activities")
    @ApiDoc
    public ApiResult<PageResult<ActivityVo>> getFavoriteActivityList(ActivityListForm pageForm){
        if(pageForm.getStatus() != null){
            PageResult pageResult = registrationService.getMyRegistrationActivityByStatus(pageForm);
            return ApiResult.ok(pageResult);
        }
        PageResult pageResult = registrationService.getMyRegistrationActivity(pageForm);
        return ApiResult.ok(pageResult);
    }
}
