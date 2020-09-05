package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.ScanResult;
import com.worldelite.job.form.*;
import com.worldelite.job.service.DictService;
import com.worldelite.job.service.IContentScanner;
import com.worldelite.job.service.JobService;
import com.worldelite.job.service.SmartTipsService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.vo.*;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 各搜索框智能提示接口
 *
 * @author 邓集阶
 */
@RestController
@RequestMapping("/api/smarttips/")
@Validated
@Slf4j
public class SmartTipsApi extends BaseApi {

    @Autowired
    private SmartTipsService smartTipsService;

    /**
     * 职位搜索框智能提示
     * @param keyword 用户在搜索框输入的关键字
     * @return
     */
    @GetMapping("search-job-tips")
    @ApiDoc
    public SearchJobTipsVo searchJobTips(@RequestParam  String keyword){
        return smartTipsService.searchJobTips(keyword);
    }

}
