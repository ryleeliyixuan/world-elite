package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.service.FileService;
import com.worldelite.job.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RequestMapping("/api/file/")
@RestController
public class FileApi extends BaseController {

    @Autowired
    private FileService fileService;

    /**
     * 下载临时目录的文件
     *
     * @param response
     * @param fileKey
     */
    @RequireLogin
    @GetMapping("download/{fileKey}")
    public void downloadFile(HttpServletResponse response, @PathVariable String fileKey, @RequestParam String fileName){
        File file = fileService.getFile(fileKey);
        ResponseUtils.writeFile(response, file, fileName);
    }

}
