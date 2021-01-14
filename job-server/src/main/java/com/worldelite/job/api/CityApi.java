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
public class CityApi extends BaseApi{

    @Autowired
    private CityService cityService;

    /**
     * 获取所有城市数据
     * @param type 城市类型
     * @return
     */
    @ApiDoc
    @GetMapping("list")
    public ApiResult<List<CityVo>> list(@RequestParam Byte type){
        List<CityVo> cityVoList = cityService.getCacheTree(type);
        return ApiResult.ok(cityVoList);
    }

    @PostMapping("get-city-id-by-name")
    public ApiResult<List<Integer>> getCityIdByFuzzy(@RequestBody CityForm cityForm) {
        List<Integer> cityId = cityService.getCityId(cityForm.getCityNames());
        return ApiResult.ok(cityId);
    }

}
