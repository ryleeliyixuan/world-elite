package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.ResumeType;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.ResumeDetail;
import com.worldelite.job.form.*;
import com.worldelite.job.service.*;
import com.worldelite.job.service.resume.ResumeService;
import com.worldelite.job.service.resume.ResumeServiceFactory;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private ResumeEduService resumeEduService;

    @Autowired
    private ResumeExpService resumeExpService;

    @Autowired
    private ResumeLanguageService resumeLanguageService;
    
    @Autowired
    private ResumeCertificateService resumeCertificateService;

    @Autowired
    private ResumePracticeService resumePracticeService;

    @Autowired
    private ResumeSkillService resumeSkillService;

    @Autowired
    private ResumeLinkService resumeLinkService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ResumeAttachService resumeAttachService;

    @Autowired
    private JobApplyService jobApplyService;

    @Autowired
    private ResumeCollectService resumeCollectService;

    @Autowired
    private ResumeShareService resumeShareService;

    /**
     * 我的简历信息
     * @return
     */
    @RequireLogin
    @GetMapping("my-resume")
    @ApiDoc
    public ApiResult<List<ResumeVo>> myResume() {
//        ResumeService resumeService = ResumeServiceFactory.getResumeService(type);
        ResumeService resumeService = ResumeServiceFactory.getDefaultService();
//        ResumeDetail resumeDetail = resumeService.getDefaultOrCreate();
        List<ResumeDetail> resumeDetails = resumeService.getDefaultListOrCreate();
        List<ResumeVo>resumeVos = new ArrayList<>();
        for (ResumeDetail one:resumeDetails){
            resumeVos.add(resumeService.toResumeVo(one));
        }
        return ApiResult.ok(resumeVos);
//        return ApiResult.ok(resumeService.toResumeVo(resumeDetail));
    }

    /**
     * 获取简历列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin
    @PostMapping("list")
    @ApiDoc
    public ApiResult<PageResult<ResumeVo>> getResumeList(@RequestBody ResumeListForm listForm){
//        ResumeService resumeService = ResumeServiceFactory.getResumeService(listForm.getType());
//        PageResult<ResumeDetail> pageResult = resumeService.list(listForm);
//        return ApiResult.ok(resumeService.toResumeVo(pageResult));
        ResumeService resumeService = ResumeServiceFactory.getResumeService(ResumeType.GENERAL.value);
        PageResult<ResumeDetail> pageResult = resumeService.search(listForm);
        return ApiResult.ok(resumeService.toResumeVo(pageResult));
    }

    /**
     * 搜索简历
     * @param listForm
     * @return
     */
    @RequireLogin
    @PostMapping("search")
    @ApiDoc
    public ApiResult<PageResult<ResumeVo>> searchResume(@RequestBody ResumeListForm listForm){
        ResumeService resumeService = ResumeServiceFactory.getResumeService(listForm.getType());
        PageResult<ResumeDetail> pageResult = resumeService.search(listForm);
        return ApiResult.ok(resumeService.toResumeVo(pageResult));
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
        ResumeService resumeService = ResumeServiceFactory.getResumeService(id);
        ResumeDetail resumeDetail = resumeService.getResumeDetail(id);
        return ApiResult.ok(resumeService.toResumeVo(resumeDetail));
    }

    /**
     * 保存基本信息
     * this one has been updated
     * @param resumeForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-basic")
    @ApiDoc
    public ApiResult<ResumeVo> saveBasic(@RequestBody ResumeForm resumeForm) {
        ResumeService resumeService = ResumeServiceFactory.getResumeService(resumeForm.getType());
        ResumeDetail resumeDetail = resumeService.saveBasic(resumeForm);
        return ApiResult.ok(resumeService.toResumeVo(resumeDetail));
    }

    /**
     * add resume
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("add-resume")
    @ApiDoc
    public ApiResult<ResumeVo> addResume() {
        ResumeService resumeService = ResumeServiceFactory.getDefaultService();
        ResumeDetail resumeDetail = resumeService.addResume();
        return ApiResult.ok(resumeService.toResumeVo(resumeDetail));
    }

    /**
     * delete resume
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume")
    @ApiDoc
    public ApiResult deleteResume(@RequestParam Long id) {
        ResumeService resumeService = ResumeServiceFactory.getDefaultService();
        resumeService.deleteResume(id);
        return ApiResult.ok();

    }

    /**
     * 删除附件简历
     * don't use
     * @param resumeId 简历ID
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-attachment")
    @ApiDoc
    public ApiResult delResumeAttachment(@RequestParam Long resumeId){
        //删除简历附件的逻辑是一样的，返回默认的简历服务类即可
        ResumeService resumeService = ResumeServiceFactory.getDefaultService();
        resumeService.deleteResumeAttachment(resumeId);
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
        if (resumeEduForm.getGpa() != null &&
                (resumeEduForm.getGpa() < 0 || resumeEduForm.getGpa() > 4)) {
            return ApiResult.fail(ApiCode.INVALID_PARAM, message("edit.resume.edu.gpa.range.error"));
        }
        resumeEduForm.setGpa(resumeEduForm.getGpa() == null ? 0.0 : resumeEduForm.getGpa());
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
    @ApiDoc
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
     * save language
     * @param resumeLanguageForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-language")
    @ApiDoc
    public ApiResult<ResumeLanguageVo> saveResumeLanguage(@Valid @RequestBody ResumeLanguageForm resumeLanguageForm) {
        ResumeLanguageVo resumeLanguageVo = resumeLanguageService.saveResumeLanguage(resumeLanguageForm);
        return ApiResult.ok(resumeLanguageVo);
    }

    /**
     * delete language
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-language")
    @ApiDoc
    public ApiResult deleteResumeLanguage(@RequestParam Integer id) {
        resumeLanguageService.deleteResumeLanguage(id);
        return ApiResult.ok();
    }

    /**
     * save certificate
     * @param resumeCertificateForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("save-resume-certificate")
    @ApiDoc
    public ApiResult<ResumeCertificateVo> saveResumeCertificate(@Valid @RequestBody ResumeCertificateForm resumeCertificateForm) {
        ResumeCertificateVo resumeCertificateVo = resumeCertificateService.saveResumeCertificate(resumeCertificateForm);
        return ApiResult.ok(resumeCertificateVo);
    }

    /**
     * delete certificate
     * @param id
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("del-resume-certificate")
    @ApiDoc
    public ApiResult deleteResumeCertificate(@RequestParam Integer id) {
        resumeCertificateService.deleteResumeCertificate(id);
        return ApiResult.ok();
    }

    /**
     * update priority
     * @param id
     * @param order
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("update-priority")
    @ApiDoc
    public ApiResult updatePriority(@RequestParam Long id, @RequestParam Byte order){
        ResumeService resumeService = ResumeServiceFactory.getDefaultService();
        resumeService.updatePriority(id, order);
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
        PageResult pageResult = jobApplyService.getApplyResumeList(listForm);
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
        PageResult<ApplyResumeVo> pageResult = jobApplyService.getApplyResumeList(listForm);
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
        jobApplyService.handleApplyResume(applyResumeForm);
        return ApiResult.ok();
    }

    /**
     * 重建全部索引
     * @return
     */
    @PostMapping("rebuild-attachment-index")
    public ApiResult rebuildAttachmentIndex(){
        //resumeApplicantService.rebuildAllIndex();
        return ApiResult.ok();
    }

    /**
     * 从简历数据库重建所有索引文件
     * @return
     */
    @PostMapping("rebuild-resume-index")
    @ApiDoc
    public ApiResult rebuildIndexFromResume(){
        //用户简历
        ResumeService resumeService = ResumeServiceFactory.getResumeService(ResumeType.GENERAL.value);
        resumeService.rebuildAllIndex();
        //企业简历
        resumeService = ResumeServiceFactory.getResumeService(ResumeType.COMPANY.value);
        resumeService.rebuildAllIndex();
        return ApiResult.ok();
    }

    //Todo 待前端更新后删除
    /**
     * 搜索附件简历返回在线简历
     * @param listForm
     * @return
     */
    @RequireLogin
    @PostMapping("list-by-attachment")
    @ApiDoc
    public ApiResult<PageResult<ResumeVo>> getResumeListByAttachment(@RequestBody ResumeAttachmentForm listForm){
        PageResult pageResult = searchService.searchResumeAttachment(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 搜索附件简历返回附件文本
     * @param listForm
     * @return
     */
    // @RequireLogin
    @PostMapping("list-attachment")
    @ApiDoc
    public ApiResult<PageResult<ResumeAttachVo>> getResumeAttachmentList(@RequestBody ResumeAttachmentForm listForm){
        PageResult pageResult = searchService.searchResumeAttachment2(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 收藏或取消收藏简历
     * @param resumeId
     * @return
     */
    @ApiDoc
    @PostMapping("collect")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult<ResumeVo> resumeCollect(@RequestParam Long resumeId){
        ResumeVo resumeDetail = resumeCollectService.collect(resumeId);
        return ApiResult.ok(resumeDetail);
    }

    /**
     * 获取简历收藏列表
     * @param listForm
     * @return
     */
    @ApiDoc
    @PostMapping("list-collect")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult listResumeCollect(@RequestBody ResumeListForm listForm){
        PageResult<ResumeVo> pageResult = resumeCollectService.list(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 简历分享
     * @param resumeShareForm
     * @return
     */
    @ApiDoc
    @PostMapping("share")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult share(@RequestBody ResumeShareForm resumeShareForm){
        resumeShareService.share(resumeShareForm);
        return ApiResult.ok();
    }

    @ApiDoc
    @PostMapping("parse-attachment")
    @RequireLogin
    public ApiResult<ResumeVo> parseAttachment(@RequestBody ParseAttachmentForm parseAttachmentForm){
        ResumeService resumeService = ResumeServiceFactory.getDefaultService();
        ResumeVo resumeVo = resumeService.parseAttachment(parseAttachmentForm);
        return ApiResult.ok(resumeVo);
    }
}
