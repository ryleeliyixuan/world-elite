package com.worldelite.job.api;

import com.worldelite.job.constants.UserType;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.*;
import com.worldelite.job.service.*;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 简历接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/resume/")
@Validated
public class ResumeApi extends BaseApi {

    @Autowired
    @Lazy
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
     * 我的简历信息
     *
     * @return
     */
    @RequireLogin
    @GetMapping("my-resume")
    @ApiDoc
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
    @ApiDoc
    public ApiResult<PageResult<ResumeVo>> getResumeList(@RequestBody ResumeListForm listForm){
        PageResult pageResult = resumeService.getResumeList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 查看简历详情
     *
     * @param id 简历ID
     * @return
     */
    @RequireLogin
    @GetMapping("resume-detail")
    @ApiDoc
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
    @ApiDoc
    public ApiResult<ResumeVo> saveBasic(@RequestBody ResumeForm resumeForm) {
        ResumeVo resumeVo = resumeService.saveBasic(resumeForm);
        return ApiResult.ok(resumeVo);
    }

    /**
     * 删除附件简历
     *
     * @param resumeId 简历ID
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-attachment")
    @ApiDoc
    public ApiResult delResumeAttachment(@RequestParam Long resumeId){
        resumeService.delResumeAttachment(resumeId);
        return ApiResult.ok();
    }

    /**
     * 保存教育经历
     *
     * @param resumeEduForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-edu")
    @ApiDoc
    public ApiResult<ResumeEduVo> saveResumeEdu(@Valid @RequestBody ResumeEduForm resumeEduForm) {
        if(resumeEduForm.getGpa() != null && resumeEduForm.getGpa() < 0){
            return ApiResult.fail(ApiCode.INVALID_PARAM, message("edit.resume.edu.gpa.below.zero"));
        }
        ResumeEduVo resumeEduVo = resumeEduService.saveResumeEdu(resumeEduForm);
        return ApiResult.ok(resumeEduVo);
    }

    /**
     * 删除教育经历
     *
     * @param id 教育经历ID
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
    @ApiDoc
    public ApiResult<ResumeExpVo> saveResumeExp(@Valid @RequestBody ResumeExpForm resumeExpForm) {
        ResumeExpVo resumeExpVo = resumeExpService.saveResumeExp(resumeExpForm);
        return ApiResult.ok(resumeExpVo);
    }

    /**
     * 删除工作经验
     *
     * @param id 工作经验ID
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-exp")
    @ApiDoc
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
    @ApiDoc
    public ApiResult<ResumePracticeVo> saveResumePractice(@RequestBody ResumePracticeForm practiceForm) {
        ResumePracticeVo resumePracticeVo = resumePracticeService.saveResumePractice(practiceForm);
        return ApiResult.ok(resumePracticeVo);
    }

    /**
     * 删除实践经验
     *
     * @param id 实践ID
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-practice")
    @ApiDoc
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
    @ApiDoc
    public ApiResult<List<ResumeSkillVo>> saveResumeSkills(@RequestBody ResumeSkillForm resumeSkillForm) {
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
    @ApiDoc
    public ApiResult<ResumeLinkVo> saveResumeLink(@RequestBody ResumeLinkForm resumeLinkForm) {
        ResumeLinkVo resumeLinkVo = resumeLinkService.saveResumeLink(resumeLinkForm);
        return ApiResult.ok(resumeLinkVo);
    }

    /**
     * 删除简历链接
     *
     * @param id 链接ID
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-link")
    @ApiDoc
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
    @ApiDoc
    public ApiResult<PageResult<ApplyResumeVo>> getUserApplyResumeList(@RequestBody ApplyResumeListForm listForm) {
        listForm.setCreatorId(curUser().getId());
        PageResult pageResult = resumeService.getApplyResumeList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取申请的简历列表
     *
     * @param listForm
     * @return
     */
    @PostMapping("apply-resume-list")
    @RequireLogin(allow = UserType.ADMIN)
    @ApiDoc
    public ApiResult<PageResult<ApplyResumeVo>> getApplyResumeList(@RequestBody ApplyResumeListForm listForm) {
        PageResult<ApplyResumeVo> pageResult = resumeService.getApplyResumeList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 处理简历
     *
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("handle-apply-resume")
    @ApiDoc
    public ApiResult handleApplyResume(@RequestBody JobApplyForm applyResumeForm) {
        resumeService.handleApplyResume(applyResumeForm);
        return ApiResult.ok();
    }
}
