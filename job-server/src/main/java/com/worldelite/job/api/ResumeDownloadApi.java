package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.entity.ResumeDownload;
import com.worldelite.job.form.ResumeDownloadForm;
import com.worldelite.job.form.ResumeDownloadListForm;
import com.worldelite.job.service.FileService;
import com.worldelite.job.service.ResumeDownloadService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeDownloadVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 简历下载信息接口
 */
@RestController
@RequestMapping("/api/resume-download/")
public class ResumeDownloadApi extends BaseController {

    @Autowired
    private ResumeDownloadService resumeDownloadService;

    /**
     * 保存简历下载信息
     * @param resumeDownloadForm
     * @return
     */
    @ApiDoc
    @PostMapping("save")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult<ResumeDownloadVo> save(@RequestBody ResumeDownloadForm resumeDownloadForm){
        ResumeDownload resumeDownload = resumeDownloadService.save(resumeDownloadForm);
        return ApiResult.ok(resumeDownloadService.toVo(resumeDownload));
    }

    /**
     * 获取我的下载列表
     * @param listForm
     * @return
     */
    @ApiDoc
    @PostMapping("my-download")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult<PageResult<ResumeDownloadVo>> myDownload(@RequestBody ResumeDownloadListForm listForm){
        PageResult<ResumeDownloadVo> pageResult = resumeDownloadService.list(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 删除下载信息
     * @param id 下载ID
     * @return
     */
    @ApiDoc
    @PostMapping("delete")
    @RequireLogin(allow = UserType.COMPANY)
    public ApiResult delete(@RequestParam Integer id){
        resumeDownloadService.delete(id);
        return ApiResult.ok();
    }
}
