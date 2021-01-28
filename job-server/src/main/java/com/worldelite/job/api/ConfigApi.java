package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.*;
import com.worldelite.job.service.ConfigCarouselService;
import com.worldelite.job.service.ConfigModuleService;
import com.worldelite.job.service.ConfigService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.ConfigCarouselVo;
import com.worldelite.job.vo.ConfigModuleVo;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import me.zhyd.oauth.request.AuthPinterestRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ConfigModuleService configModuleService;

    @Autowired
    private ConfigCarouselService configCarouselService;

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
     * 保存推荐板块配置
     * @param configForm
     */
    @RequireLogin(allow = UserType.ADMIN)
    @PostMapping("save-recommend-config")
    @ApiDoc
    public ApiResult saveRecommendConfig(@RequestBody RecommendConfigForm configForm){
        configService.saveRecommendConfig(configForm);
        return ApiResult.ok();
    }

    /**
     * 获取推荐板块配置
     *
     * @param configType 配置类型
     * @return
     */
    @GetMapping("get-recommend-config")
    @ApiDoc
    public ApiResult<RecommendConfigForm> getRecommendConfig(@RequestParam Integer configType){
        RecommendConfigForm webHomeConfigForm = configService.getRecommendConfig(configType);
        return ApiResult.ok(webHomeConfigForm);
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

    /**
     * 更新模块信息
     * @param configModuleForm 模块表单
     * @return
     */
    @ApiDoc
    @PostMapping("update-module")
    public ApiResult updateModule(@RequestBody ConfigModuleForm configModuleForm){
        configModuleService.updateConfigModule(configModuleForm);
        return ApiResult.ok();
    }

    /**
     * 查询模块信息
     * @param configModuleListForm 模块表单
     * @return
     */
    @ApiDoc
    @PostMapping("list-module")
    public ApiResult<PageResult<ConfigModuleVo>> listModule(@RequestBody ConfigModuleListForm configModuleListForm){
        PageResult<ConfigModuleVo> pageResult = configModuleService.list(configModuleListForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 分页查询轮播图
     * @param pageForm
     * @return
     */
    @ApiDoc
    @GetMapping("list-carousel")
    public ApiResult<PageResult<ConfigCarouselVo>> listCarousel(PageForm pageForm){
        PageResult<ConfigCarouselVo> pageResult = configCarouselService.list(pageForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 添加轮播图
     * @param configCarouselForm
     * @return
     */
    @ApiDoc
    @PostMapping("add-carousel")
    public ApiResult addCarousel(@RequestBody ConfigCarouselForm configCarouselForm){
        configCarouselService.add(configCarouselForm);
        return ApiResult.ok();
    }

    /**
     * 删除轮播图
     * @param id
     * @return
     */
    @ApiDoc
    @PostMapping("delete-carousel")
    public ApiResult deleteCarousel(@RequestParam Integer id){
        configCarouselService.delete(id);
        return ApiResult.ok();
    }

    /**
     * 上升轮播图
     * @param id
     * @return
     */
    @ApiDoc
    @PostMapping("move-up-carousel")
    public ApiResult moveUpCarousel(@RequestParam Integer id){
        configCarouselService.moveUp(id);
        return ApiResult.ok();
    }

    /**
     * 下降轮播图
     * @param id
     * @return
     */
    @ApiDoc
    @PostMapping("move-down-carousel")
    public ApiResult moveDownCarousel(@RequestParam Integer id){
        configCarouselService.moveDown(id);
        return ApiResult.ok();
    }
}
