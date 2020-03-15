package com.worldelite.job.api;

import com.worldelite.job.service.CountryService;
import com.worldelite.job.util.RequestUtils;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.CountryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/country")
public class CountryApi {

    @Autowired
    private CountryService countryService;


    /**
     * 获取所有国家代码
     *
     * @return
     */
    @GetMapping("get-all")
    public ApiResult getAllCountry(){
        List<CountryVo> countryList = countryService.getAll();
        return ApiResult.ok(countryList);
    }

    /**
     * 获取当前IP所在的国家代码
     * @param request
     * @return
     */
    @GetMapping("current")
    public ApiResult getCurrentCountry(HttpServletRequest request){
        CountryVo countryVo = countryService.getCountryByIp(RequestUtils.getClientIP(request));
        return ApiResult.ok(countryVo);
    }
}
