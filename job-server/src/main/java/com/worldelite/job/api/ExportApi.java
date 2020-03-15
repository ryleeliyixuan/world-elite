package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.service.ExportService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
