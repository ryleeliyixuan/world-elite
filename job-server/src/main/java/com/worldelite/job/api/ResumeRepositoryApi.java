package com.worldelite.job.api;

import com.alibaba.excel.util.StringUtils;
import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.ResumeRepository;
import com.worldelite.job.form.*;
import com.worldelite.job.service.*;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 企业简历库接口
 */
@RestController
@RequestMapping("/api/resume-repository/")
public class ResumeRepositoryApi extends BaseApi{

    @Autowired
    private ResumeRepositoryService resumeRepositoryService;

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

    @Autowired
    private UserExpectJobService userExpectJobService;

    /**
     * 通过解析附件简历新增简历库数据
     * @param attachmentName 简历文件路径
     * @return
     */
    @ApiDoc
    @PostMapping("save-attachment")
    public ApiResult saveAttachment(@RequestParam String attachmentName){
        resumeRepositoryService.saveAttachment(attachmentName);
        return ApiResult.ok();
    }

    /**
     * 给简历库数据添加简历附件
     * @param id 简历库中简历ID
     * @param attachmentName 附件文件路径
     * @return
     */
    @ApiDoc
    @PostMapping("add-attachment")
    public ApiResult addAttachment(@RequestParam Long id,@RequestParam String attachmentName){
        //resumeRepositoryService.addAttachment(id,attachmentName);
        return ApiResult.ok();
    }

    /**
     * 保存简历
     * @param resumeRepositoryForm 简历信息
     * @return
     */
    @ApiDoc
    @PostMapping("add-from-form")
    public ApiResult addResume(@RequestBody ResumeRepositoryForm resumeRepositoryForm){
        //resumeRepositoryService.addResume(resumeRepositoryForm);
        return ApiResult.ok();
    }

    /**
     * 将简历库简历添加到职位申请
     * @param resumeId 简历ID
     * @param jobId 职位ID
     * @param status 申请状态，2为已查看，6为不合适
     * @return
     */
    @ApiDoc
    @PostMapping("add-from-user")
    public ApiResult addResume(@RequestParam Long resumeId,@RequestParam Long jobId,@RequestParam Byte status){
        resumeRepositoryService.addToJobApply(resumeId,jobId,status);
        return ApiResult.ok();
    }

    /**
     * 将简历添加到指定企业简历库
     * @param originId 源简历
     * @param companyId 目标企业
     * @param type 添加类型：1、从用户简历添加（默认）；2、从企业简历库添加
     * @return
     */
    @ApiDoc
    @PostMapping("add-resume")
    public ApiResult addResume(Long originId,Long companyId,int type){
        switch (type){
            case 2:
                //resumeRepositoryService.addFromRepository(originId,companyId);
                break;
            default:
                //resumeRepositoryService.addFromUserResume(originId,companyId);
                break;
        }
        return ApiResult.ok();
    }

    /**
     * 搜索简历库
     * @param listForm 过滤条件
     * @return
     */
    @ApiDoc
    @PostMapping("search")
    public ApiResult<PageResult<ResumeVo>> search(@RequestBody ResumeListForm listForm){
//        if(StringUtils.isEmpty(resumeRepositoryListForm.getKeyword())){
//            PageResult<ResumeRepositoryVo> pageResult = resumeRepositoryService.search(resumeRepositoryListForm);
//            return ApiResult.ok(pageResult);
//        }else{
//            PageResult<ResumeRepositoryVo> pageResult = resumeRepositoryService.searchByKeyword(resumeRepositoryListForm);
//            return ApiResult.ok(pageResult);
//        }

        PageResult<ResumeVo> pageResult = resumeRepositoryService.getResumeList(listForm);
        return ApiResult.ok(pageResult);
    }

    public void suggest(){

    }

    public void delete(){

    }

    /***************** 新简历库逻辑 *********************/

    /**
     * 通过简历ID获取简历
     * @param resumeId 简历ID
     * @return
     */
    @RequireLogin
    @GetMapping("resume")
    @ApiDoc
    public ApiResult<ResumeVo> resume(@RequestParam Long resumeId) {
        ResumeVo resumeVo = resumeRepositoryService.getResumeDetail(resumeId);
        return ApiResult.ok(resumeVo);
    }

    /**
     * 保存基本信息
     *
     * @param resumeForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save-resume-basic")
    @ApiDoc
    public ApiResult<ResumeVo> saveBasic(@RequestBody ResumeForm resumeForm) {
        ResumeVo resumeVo = resumeRepositoryService.saveBasic(resumeForm);
        return ApiResult.ok(resumeVo);
    }

    /**
     * 删除附件简历
     *
     * @param resumeId 简历ID
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("del-resume-attachment")
    @ApiDoc
    public ApiResult delResumeAttachment(@RequestParam Long resumeId){
        resumeRepositoryService.delResumeAttachment(resumeId);
        return ApiResult.ok();
    }

    /**
     * 保存教育经历
     *
     * @param resumeEduForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
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
    @RequireLogin(allow = UserType.COMPANY)
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
    @RequireLogin(allow = UserType.COMPANY)
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
    @RequireLogin(allow = UserType.COMPANY)
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
    @RequireLogin(allow = UserType.COMPANY)
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
    @RequireLogin(allow = UserType.COMPANY)
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
    @RequireLogin(allow = UserType.COMPANY)
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
    @RequireLogin(allow = UserType.COMPANY)
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
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("del-resume-link")
    @ApiDoc
    public ApiResult delResumeLink(@RequestParam Integer id) {
        resumeLinkService.deleteResumeLink(id);
        return ApiResult.ok();
    }

    /**
     * 保存用户求职意向
     * @param expectJobForm
     * @return
     */
    @RequireLogin(allow = UserType.COMPANY)
    @PostMapping("save-expect-job")
    @ApiDoc
    public ApiResult<UserExpectJobVo> saveUserExpectJob(@RequestBody UserExpectJobForm expectJobForm){
        UserExpectJobVo userExpectJobVo = userExpectJobService.saveUserExpectJob(expectJobForm);
        return ApiResult.ok(userExpectJobVo);
    }
}
