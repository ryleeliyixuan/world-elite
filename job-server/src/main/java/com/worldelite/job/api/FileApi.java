package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.service.FileService;
import com.worldelite.job.util.ResponseUtils;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 文件接口
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
     * @param fileKey  下载文件
     * @param fileName 文件命名
     */
    @RequireLogin
    @GetMapping("download/{fileKey}")
    @ApiDoc
    public void downloadFile(HttpServletResponse response, @PathVariable String fileKey, @RequestParam String fileName){
        File file = fileService.getFile(fileKey);
        ResponseUtils.writeFile(response, file, fileName);
    }

}
