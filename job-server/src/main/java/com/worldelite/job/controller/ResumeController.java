package com.worldelite.job.controller;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.service.ResumeService;
import com.worldelite.job.vo.ResumeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Controller
public class ResumeController extends BaseController{

    @Autowired
    @Lazy
    private ResumeService resumeService;

    /**
     * 导出简历模板
     *
     * @param resumeId
     * @return
     */
    @RequireLogin
    @GetMapping("/resume/{resumeId}")
    public String resume(ModelMap modelMap, @PathVariable("resumeId") Long resumeId){
        ResumeVo resumeVo = resumeService.getResumeDetail(resumeId);
        modelMap.put("resume", resumeVo);
        return "resume";
    }
}
