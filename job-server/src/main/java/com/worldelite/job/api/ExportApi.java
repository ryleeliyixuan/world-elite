package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.*;
import com.worldelite.job.mq.ExportMessageType;
import com.worldelite.job.service.ExportService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 导出数据接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RequestMapping("/api/export/")
@RestController
public class ExportApi extends BaseApi{


    @Autowired
    private ExportService exportService;

    /**
     * 把简历导出PDF
     * @param resumeId 简历ID
     * @return
     */
    @RequireLogin
    @GetMapping("resume-to-pdf")
    @ApiDoc
    public ApiResult exportResumeToPdf(@RequestParam Long resumeId){
        String fileName = exportService.exportResumeToPdf(resumeId);
        return ApiResult.ok(fileName);
    }

    /**
     * 把活动报名信息导出PDF
     * @param registrationId 活动报名ID
     * @return
     */
    @RequireLogin
    @GetMapping("registration-to-pdf")
    @ApiDoc
    public ApiResult exportResumeToPdf(@RequestParam Integer registrationId){
        String fileName = exportService.exportRegistrationToPdf(registrationId);
        return ApiResult.ok(fileName);
    }

    /**
     * 导出用户列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("export-user-list")
    @ApiDoc
    public ApiResult exportUserList(@RequestBody UserListForm listForm){
        exportService.queueExportExcel(ExportMessageType.EXPORT_USER, listForm);
        return ApiResult.ok();
    }

    /**
     * 导出简历列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("export-resume-list")
    @ApiDoc
    public ApiResult exportResumeList(@RequestBody ResumeListForm listForm){
        exportService.queueExportExcel(ExportMessageType.EXPORT_RESUME, listForm);
        return ApiResult.ok();
    }

    /**
     * 导出职位列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("export-job-list")
    @ApiDoc
    public ApiResult exportJobList(@RequestBody JobListForm listForm){
        exportService.queueExportExcel(ExportMessageType.EXPORT_JOB, listForm);
        return ApiResult.ok();
    }

    /***
     * 导出报名列表
     * @param listForm
     * @return
     */
    @RequireLogin
    @PostMapping("export-registration-list")
    @ApiDoc
    public ApiResult<String> exportJobList(@RequestBody RegistrationListForm listForm){
        String fileName = exportService.exportRegistrationToExcel(listForm);
        return ApiResult.ok(fileName);
    }
}
