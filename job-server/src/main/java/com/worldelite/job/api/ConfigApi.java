package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.HomeConfigForm;
import com.worldelite.job.service.ConfigService;
import com.worldelite.job.vo.ApiResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * 配置接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/config/")
public class ConfigApi {

    @Autowired
    private ConfigService configService;

    /**
     * 保存首页文案配置
     *
     * @param configForm
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("save-home-config")
    @ApiDoc
    public ApiResult saveHomeConfig(@RequestBody HomeConfigForm configForm){
        configService.saveHomeConfig(configForm);
        return ApiResult.ok();
    }

    /**
     * 获取首页文案配置
     *
     * @param configType 配置类型
     * @return
     */
    @GetMapping("get-home-config")
    @ApiDoc
    public ApiResult<HomeConfigForm> getHomeConfig(@RequestParam Integer configType){
        HomeConfigForm homeConfigForm = configService.getHomeConfig(configType);
        return ApiResult.ok(homeConfigForm);
    }


    /**
     * 用户协议
     *
     * @return
     */
    @ApiDoc
    @GetMapping("user-protocol")
    public ApiResult getUserProtocol(){
        String protocol = configService.getUserProtocol();
        return ApiResult.ok(protocol);
    }
}
