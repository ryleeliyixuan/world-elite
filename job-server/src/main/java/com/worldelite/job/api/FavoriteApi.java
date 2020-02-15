package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.FavoriteForm;
import com.worldelite.job.service.FavoriteService;
import com.worldelite.job.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResult favorite(@Valid @RequestBody FavoriteForm favoriteForm){
        Boolean result = favoriteService.favorite(favoriteForm);
        return ApiResult.ok(result);
    }
}
