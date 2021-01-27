package com.worldelite.job.api;

import com.worldelite.job.form.CityForm;
import com.worldelite.job.service.CityService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CityVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 城市接口
 *
 * @author 邓集阶
 */
@RestController
@RequestMapping("/api/city/")
public class CityApi extends BaseApi {

    @Autowired
    private CityService cityService;

    /**
     * 获取所有城市数据
     *
     * @param type 城市类型
     * @return
     */
    @ApiDoc
    @GetMapping("list")
    public ApiResult<List<CityVo>> list(@RequestParam Byte type) {
        List<CityVo> cityVoList = cityService.getCacheTree(type);
        return ApiResult.ok(cityVoList);
    }

    @PostMapping("get-city-id-by-name")
    public ApiResult<List<Integer>> getCityIdByFuzzy(@RequestBody CityForm cityForm) {
        List<Integer> cityId = cityService.getCityId(cityForm.getCityNames());
        return ApiResult.ok(cityId);
    }

    @GetMapping("get-city-by-name")
    public ApiResult<List<CityVo>> list(@RequestParam String name) {
        List<CityVo> cityVoList = cityService.getCitiesByName(name);
        return ApiResult.ok(cityVoList);
    }

    /**
     * 根据城市名获取城市,到市一级
     *
     * @param name
     * @return
     */
    @GetMapping("get-to-city-level-by-name")
    public ApiResult<List<CityVo>> listToCityLevel(@RequestParam String name) {
        List<CityVo> cityVoList = cityService.getToCityLevelByName(name);
        return ApiResult.ok(cityVoList);
    }

}
