package com.worldelite.job.controller;

import com.worldelite.job.service.RegistrationService;
import com.worldelite.job.vo.QuestionnaireTemplateWithAnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 活动报名控制器
 * 主要用于导出报名信息pdf文件
 */
@Controller
public class RegistrationController extends BaseController{

    @Autowired
    private RegistrationService registrationService;

    /**
     * 导出报名信息
     *
     * @param registrationId
     * @return
     */
//    @RequireLogin
    @GetMapping("/registration/{registrationId}")
    public String resume(ModelMap modelMap, @PathVariable("registrationId") Integer registrationId){
        QuestionnaireTemplateWithAnswerVo registrationVo = registrationService.getRegistrationWithTemplateDetail(registrationId);
        modelMap.put("registration", registrationVo);
        return "registration";
    }
}
