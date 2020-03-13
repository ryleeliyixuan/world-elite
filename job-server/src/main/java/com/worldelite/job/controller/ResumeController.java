package com.worldelite.job.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @GetMapping("/resume/{resumeId}")
    public String resume(@PathVariable("resumeId") Long resumeId){
        return "/resume";
    }
}
