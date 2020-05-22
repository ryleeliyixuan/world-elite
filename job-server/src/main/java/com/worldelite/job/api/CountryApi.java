package com.worldelite.job.api;

import com.worldelite.job.service.CountryService;
import com.worldelite.job.util.RequestUtils;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CountryVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 国家数据接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/country/")
public class CountryApi {

    @Autowired
    private CountryService countryService;


    /**
     * 获取所有国家代码
     *
     * @return
     */
    @ApiDoc
    @GetMapping("get-all")
    public ApiResult<List<CountryVo> > getAllCountry(){
        List<CountryVo> countryList = countryService.getAll();
        return ApiResult.ok(countryList);
    }

    /**
     * 获取当前IP所在的国家代码
     * @return
     */
    @GetMapping("current")
    @ApiDoc
    public ApiResult<CountryVo> getCurrentCountry(HttpServletRequest request){
        CountryVo countryVo = countryService.getCountryByIp(RequestUtils.getClientIP(request));
        return ApiResult.ok(countryVo);
    }
}
