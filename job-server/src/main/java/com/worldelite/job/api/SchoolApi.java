package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.SchoolForm;
import com.worldelite.job.form.SchoolListForm;
import com.worldelite.job.service.SchoolService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.SchoolVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学校接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/school/")
@Validated
public class SchoolApi {

    @Autowired
    private SchoolService schoolService;

    /**
     * 学校列表
     *
     * @param schoolListForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<SchoolVo>> getSchoolList(SchoolListForm schoolListForm) {
        PageResult pageResult = schoolService.getSchoolList(schoolListForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 保存学校
     * @param schoolForm
     * @return
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("save")
    @ApiDoc
    public ApiResult saveSchool(@RequestBody SchoolForm schoolForm) {
        schoolService.saveSchool(schoolForm);
        return ApiResult.ok();
    }

    /**
     * 根据关键词进行模糊搜索
     * @param keyword 关键字
     * @return
     */
    @GetMapping("search")
    @ApiDoc
    public ApiResult search(@RequestParam String keyword) {
        List<SchoolVo> schoolList = schoolService.searchByName(keyword);
        return ApiResult.ok(schoolList);
    }
}
