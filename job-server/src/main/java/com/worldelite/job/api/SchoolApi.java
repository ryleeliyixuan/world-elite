package com.worldelite.job.api;

import com.worldelite.job.service.SchoolService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.SchoolVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/school")
@Validated
public class SchoolApi {

    @Autowired
    private SchoolService schoolService;

    /**
     * 根据关键词进行模糊搜索
     * @return
     */
    @GetMapping("search")
    public ApiResult search(@RequestParam String keyword){
        List<SchoolVo> schoolList = schoolService.searchByName(keyword);
        return ApiResult.ok(schoolList);
    }
}
