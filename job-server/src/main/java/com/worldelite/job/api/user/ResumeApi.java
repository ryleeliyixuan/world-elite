package com.worldelite.job.api.user;

import com.worldelite.job.api.BaseApi;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.context.anatation.RequireLogin;
import com.worldelite.job.entity.*;
import com.worldelite.job.form.ResumeEduForm;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.service.ResumeEduService;
import com.worldelite.job.service.ResumeService;
import com.worldelite.job.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 简历接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/resume")
public class ResumeApi extends BaseApi {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeEduService resumeEduService;

    /**
     * 获取我的简历信息
     *
     * @return
     */
    @RequireLogin
    @GetMapping("my-resume")
    public ApiResult<ResumeVo> myResume(){
        ResumeVo resumeVo = resumeService.getDefaultOrCreate(curUser().getId());
        return ApiResult.ok(resumeVo);
    }

    /**
     * 保存基本信息
     * @param resumeForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-basic")
    public ApiResult<ResumeVo> saveBasic(@RequestBody ResumeForm resumeForm){
        ResumeVo resumeVo = resumeService.saveBasic(resumeForm);
        return ApiResult.ok(resumeVo);
    }

    /**
     * 保存教育经历
     *
     * @param resumeEduForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-edu")
    public ApiResult<ResumeEduVo> saveResumeEdu(@RequestBody ResumeEduForm resumeEduForm){
        ResumeEduVo resumeEduVo = resumeEduService.saveResumeEdu(resumeEduForm);
        return ApiResult.ok(resumeEduVo);
    }

    /**
     * 删除教育经历
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-edu")
    public ApiResult deleteResumeEdu(@RequestParam Integer id){
        resumeEduService.deleteResumeEdu(id);
        return ApiResult.ok();
    }

    /**
     * 保存工作经验
     *
     * @param experience
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-exp")
    public ApiResult<ResumeExpVo> saveResumeExp(@RequestBody ResumeExperience experience){
        return null;
    }

    /**
     * 保存实践经验
     *
     * @param practice
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-practice")
    public ApiResult<ResumePracticeVo> saveResumePractice(@RequestBody ResumePractice practice){
        return null;
    }

    /**
     * 保存擅长技能
     *
     * @param resumeSkill
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-skill")
    public ApiResult<ResumeSkillVo> saveResumeSkill(@RequestBody ResumeSkill resumeSkill){
        return null;
    }

}
