package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.ResumeLinkForm;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.form.UserListForm;
import com.worldelite.job.mq.ExportMessageType;
import com.worldelite.job.service.ExportService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RequestMapping("/api/export")
@RestController
public class ExportApi extends BaseApi{


    @Autowired
    private ExportService exportService;

    /**
     * 把简历导出PDF
     * @return
     */
    @RequireLogin
    @GetMapping("resume-to-pdf")
    public ApiResult exportResumeToPdf(@RequestParam Long resumeId){
        String fileName = exportService.exportResumeToPdf(resumeId);
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
    public ApiResult exportResumeList(@RequestBody ResumeListForm listForm){
        exportService.queueExportExcel(ExportMessageType.EXPORT_RESUME, listForm);
        return ApiResult.ok();
    }
}
