package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.Download;
import com.worldelite.job.form.DownloadListForm;
import com.worldelite.job.service.DownloadService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 下载接口
 * @author yeguozhong yedaxia.github.com
 */
@RequestMapping("/api/download/")
@RestController
public class DownloadApi extends BaseApi{

    @Autowired
    private DownloadService downloadService;

    /**
     * 下载列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<Download>> list(DownloadListForm listForm){
        listForm.setUserId(curUser().getId());
        PageResult pageResult = downloadService.getDownloadList(listForm);
        return ApiResult.ok(pageResult);
    }
}