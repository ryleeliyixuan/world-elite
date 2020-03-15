package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.FavoriteForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.FavoriteService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/favorite")
public class FavoriteApi {

    @Autowired
    private FavoriteService favoriteService;

    /**
     * 收藏或者取消收藏
     *
     * @param favoriteForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @PostMapping("favorite")
    public ApiResult favorite(@RequestBody FavoriteForm favoriteForm){
        Boolean result = favoriteService.favorite(favoriteForm);
        return ApiResult.ok(result);
    }


    /**
     * 获取当前用户收藏的工作列表
     *
     * @param pageForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @GetMapping("get-favorite-jobs")
    public ApiResult getFavoriteJobList(PageForm pageForm){
        PageResult pageResult = favoriteService.getUserFavoriteJobList(pageForm);
        return ApiResult.ok(pageResult);
    }
}
