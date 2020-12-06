package com.worldelite.job.api;

import com.worldelite.job.form.CountForm;
import com.worldelite.job.form.FlagPairForm;
import com.worldelite.job.form.GraduateTimePairForm;
import com.worldelite.job.service.AnalysisService;
import com.worldelite.job.util.ResponseUtils;
import com.worldelite.job.vo.ApiResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nuo Xu
 */
@RestController
@RequestMapping("/api/analysis/")
public class AnalysisApi {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);
    @Autowired
    private AnalysisService analysisService;

    @ApiDoc
    @GetMapping("all-user")
    public ApiResult<Integer> getAllUserCount(){
        return ApiResult.ok(analysisService.countUsers());
    }

    @ApiDoc
    @GetMapping("all-email")
    public ApiResult<Integer> getAllEmailCount(){
        return ApiResult.ok(analysisService.countEmails());
    }

    @ApiDoc
    @GetMapping("all-resume")
    public ApiResult<Integer> getAllResumeCount(){
        return ApiResult.ok(analysisService.countResumes());
    }

    @ApiDoc
    @GetMapping("all-school")
    public ApiResult<Integer> getAllSchoolCount(){
        return ApiResult.ok(analysisService.countSchools());
    }

    @ApiDoc
    @GetMapping("school-group")
    public ApiResult<CountForm> getSchoolGroup(){
        return ApiResult.ok(analysisService.countSchoolGroupBy());
    }

    @ApiDoc
    @GetMapping("place-group")
    public ApiResult<CountForm> getPlaceGroup(){
        return ApiResult.ok(analysisService.countPlaceGroupBy());
    }

    @ApiDoc
    @GetMapping("graduate-time--group")
    public ApiResult<GraduateTimePairForm> getGraduateTimeGroup(){
        return ApiResult.ok(analysisService.countGraduateTimeGroupBy());
    }

    @ApiDoc
    @GetMapping("major-group")
    public ApiResult<CountForm> getMajorGroup(){
        return ApiResult.ok(analysisService.countMajorGroupBy());
    }

    @ApiDoc
    @GetMapping("user-application-flag--group")
    public ApiResult<FlagPairForm> getFlagGroup(){
        return ApiResult.ok(analysisService.countFlagGroupBy());
    }
}
