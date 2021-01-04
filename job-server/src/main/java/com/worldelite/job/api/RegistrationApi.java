package com.worldelite.job.api;

import com.worldelite.job.entity.Registration;
import com.worldelite.job.form.RegistrationForm;
import com.worldelite.job.service.RegistrationService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.RegistrationVo;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @PostMapping
    public ApiResult addRegistration(@RequestBody RegistrationForm registrationForm){
        registrationService.addRegistration(registrationForm);
        return ApiResult.ok();
    }

    /**
     * 获取活动对应的报名表列表
     * @param activityId 活动ID
     * @return
     */
    @ApiDoc
    @GetMapping("activity-registration/{activityId}")
    public ApiResult<PageResult<RegistrationVo>> listByActivityId(@PathVariable Integer activityId){
        PageResult<RegistrationVo> pageResult = registrationService.getRegistrationList(activityId);
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
        RegistrationVo registration = registrationService.getRegistrationDetail(id);
        return ApiResult.ok(registration);
    }


}
