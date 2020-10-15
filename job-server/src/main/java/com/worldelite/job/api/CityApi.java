package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.SchoolForm;
import com.worldelite.job.form.SchoolListForm;
import com.worldelite.job.service.CityService;
import com.worldelite.job.service.SchoolService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CityVo;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.SchoolVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 城市接口
 *
 * @author 邓集阶
 */
@RestController
@RequestMapping("/api/city/")
@Validated
public class CityApi {

    @Autowired
    private CityService cityService;

    /**
     * 获取所有城市数据
     * @param type 城市类型
     * @return
     */
    @GetMapping("list")
    public ApiResult<List<CityVo>> list(@RequestParam Byte type){
        List<CityVo> cityVoList = cityService.getCacheTree(type);
        return ApiResult.ok(cityVoList);
    }

}
