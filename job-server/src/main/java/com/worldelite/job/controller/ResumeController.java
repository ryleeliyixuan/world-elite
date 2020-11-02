package com.worldelite.job.controller;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.entity.ResumeDetail;
import com.worldelite.job.service.resume.ResumeService;
import com.worldelite.job.service.resume.ResumeServiceFactory;
import com.worldelite.job.vo.ResumeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Controller
public class ResumeController extends BaseController{

    /**
     * 导出简历模板
     *
     * @param resumeId
     * @return
     */
    @RequireLogin
    @GetMapping("/resume/{resumeId}")
    public String resume(ModelMap modelMap, @PathVariable("resumeId") Long resumeId){
        ResumeService resumeService = ResumeServiceFactory.getResumeService(resumeId);
        ResumeDetail resumeDetail = resumeService.getResumeDetail(resumeId);
        ResumeVo resumeVo = resumeService.toResumeVo(resumeDetail);
        modelMap.put("resume", resumeVo);
        return "resume";
    }
}
