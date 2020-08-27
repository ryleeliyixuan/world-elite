package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.service.ResumeUploadService;
import com.worldelite.job.vo.ApiResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历上传接口
 *
 * @author 熊文剑
 */
@RestController
@RequestMapping("/api/resume-upload/")
@Validated
public class ResumeUploadApi extends BaseApi {
    @Autowired
    ResumeUploadService resumeUploadService;

    /**
     * 读取简历内容
     *
     * @param urls 简历url
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("details")
    @ApiDoc
    public ApiResult getResumesDetail(@RequestParam String[] urls) {
        List<String> resumes = resumeUploadService.getResumesDetail(urls);
        return ApiResult.ok();
    }

    /**
     * 获取邮箱
     *
     * @param resumes 简历
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("emails")
    @ApiDoc
    public ApiResult getResumesEmail(@RequestParam List<String> resumes) {
        List<String> emails = resumeUploadService.getResumesEmail(resumes);
        return ApiResult.ok();
    }

    /**
     * 获取电话
     *
     * @param resumes 简历
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("phones")
    @ApiDoc
    public ApiResult getResumesPhone(@RequestParam List<String> resumes) {
        List<String> phones = resumeUploadService.getResumesPhone(resumes);
        return ApiResult.ok();
    }

    /**
     * 发送邀请注册邮件到用户邮箱
     *
     * @param emails 邮箱
     * @return
     */
    public ApiResult sendInviteLink(@RequestBody List<String> emails) {
        resumeUploadService.sendInviteLink(emails);
        return ApiResult.ok();
    }
}
