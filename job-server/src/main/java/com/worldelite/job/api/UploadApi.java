package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.OssDir;
import com.worldelite.job.controller.BaseController;
import com.worldelite.job.service.sdk.AliOssService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.UploadTokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 上传接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/upload")
public class UploadApi extends BaseController {

    @Autowired
    private AliOssService ossService;

    /**
     * 获取上传图片的token
     *
     * @param fileName
     * @return
     */
    @RequireLogin
    @GetMapping("pic-token")
    public ApiResult getUploadPicToken(@RequestParam String fileName){
        UploadTokenVo uploadTokenVo = ossService.getUploadToken(OssDir.PIC, fileName);
        return ApiResult.ok(uploadTokenVo);
    }

    /**
     * 获取上传附件的token
     *
     * @param fileName
     * @return
     */
    @RequireLogin
    @GetMapping("attachment-token")
    public ApiResult getUploadAttachmentToken(@RequestParam String fileName){
        UploadTokenVo uploadTokenVo = ossService.getUploadToken(OssDir.ATTACHMENT, fileName);
        return ApiResult.ok(uploadTokenVo);
    }
}
