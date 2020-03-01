package com.worldelite.job.api;

import com.worldelite.job.constants.UserType;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.*;
import com.worldelite.job.service.*;
import com.worldelite.job.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/resume")
public class ResumeApi extends BaseApi {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeEduService resumeEduService;

    @Autowired
    private ResumeExpService resumeExpService;

    @Autowired
    private ResumePracticeService resumePracticeService;

    @Autowired
    private ResumeSkillService resumeSkillService;

    @Autowired
    private ResumeLinkService resumeLinkService;

    /**
     * 获取我的简历信息
     *
     * @return
     */
    @RequireLogin
    @GetMapping("my-resume")
    public ApiResult<ResumeVo> myResume() {
        ResumeVo resumeVo = resumeService.getDefaultOrCreate(curUser().getId());
        return ApiResult.ok(resumeVo);
    }

    /**
     * 获取简历列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("list")
    public ApiResult getResumeList(@RequestBody ResumeListForm listForm){
        PageResult pageResult = resumeService.getResumeList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 查看简历详情
     *
     * @param id
     * @return
     */
    @RequireLogin
    @GetMapping("resume-detail")
    public ApiResult<ResumeVo> getResumeDetail(@RequestParam Long id) {
        ResumeVo resumeVo = resumeService.getResumeDetail(id);
        return ApiResult.ok(resumeVo);
    }

    /**
     * 保存基本信息
     *
     * @param resumeForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-basic")
    public ApiResult<ResumeVo> saveBasic(@RequestBody ResumeForm resumeForm) {
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
    public ApiResult<ResumeEduVo> saveResumeEdu(@RequestBody ResumeEduForm resumeEduForm) {
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
    public ApiResult deleteResumeEdu(@RequestParam Integer id) {
        resumeEduService.deleteResumeEdu(id);
        return ApiResult.ok();
    }

    /**
     * 保存工作经验
     *
     * @param resumeExpForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-exp")
    public ApiResult<ResumeExpVo> saveResumeExp(@RequestBody ResumeExpForm resumeExpForm) {
        ResumeExpVo resumeExpVo = resumeExpService.saveResumeExp(resumeExpForm);
        return ApiResult.ok(resumeExpVo);
    }

    /**
     * 删除工作经验
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-exp")
    public ApiResult deleteResumeEpx(@RequestParam Integer id) {
        resumeExpService.deleteResumeExp(id);
        return ApiResult.ok();
    }

    /**
     * 保存实践经验
     *
     * @param practiceForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-practice")
    public ApiResult<ResumePracticeVo> saveResumePractice(@RequestBody ResumePracticeForm practiceForm) {
        ResumePracticeVo resumePracticeVo = resumePracticeService.saveResumePractice(practiceForm);
        return ApiResult.ok(resumePracticeVo);
    }

    /**
     * 删除实践经验
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-practice")
    public ApiResult deleteResumePractice(@RequestParam Integer id) {
        resumePracticeService.deleteResumePractice(id);
        return ApiResult.ok();
    }

    /**
     * 保存擅长技能
     *
     * @param resumeSkillForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-skills")
    public ApiResult saveResumeSkills(@RequestBody ResumeSkillForm resumeSkillForm) {
        List<ResumeSkillVo> resumeSkillVoList = resumeSkillService.saveResumeSkill(resumeSkillForm);
        return ApiResult.ok(resumeSkillVoList);
    }

    /**
     * 保存简历链接
     *
     * @param resumeLinkForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-link")
    public ApiResult saveResumeLink(@RequestBody ResumeLinkForm resumeLinkForm) {
        ResumeLinkVo resumeLinkVo = resumeLinkService.saveResumeLink(resumeLinkForm);
        return ApiResult.ok(resumeLinkVo);
    }

    /**
     * 删除简历链接
     *
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-link")
    public ApiResult delResumeLink(@RequestParam Integer id) {
        resumeLinkService.deleteResumeLink(id);
        return ApiResult.ok();
    }


    /**
     * 获取当前企业用户需要处理的简历列表
     *
     * @param listForm
     * @return
     */
    @PostMapping("my-apply-resume-list")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult getUserApplyResumeList(@RequestBody ApplyResumeListForm listForm) {
        PageResult pageResult = resumeService.getUserApplyResumeList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 处理简历
     *
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("handle-apply-resume")
    public ApiResult handleApplyResume(@RequestBody JobApplyForm applyResumeForm) {
        resumeService.handleApplyResume(applyResumeForm);
        return ApiResult.ok();
    }
}
