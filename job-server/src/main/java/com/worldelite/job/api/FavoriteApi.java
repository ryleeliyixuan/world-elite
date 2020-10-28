package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.form.*;
import com.worldelite.job.service.FavoriteService;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * 收藏接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/favorite/")
public class FavoriteApi extends BaseApi{

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
    @ApiDoc
    public ApiResult favorite(@RequestBody FavoriteForm favoriteForm){
        Boolean result = favoriteService.favorite(favoriteForm);
        return ApiResult.ok(result);
    }


    /**
     * 获取当前用户收藏列表
     *
     * @param listForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @GetMapping("my-favorite-list")
    @ApiDoc
    public ApiResult getFavoriteJobList(FavoriteListForm listForm){
        PageResult pageResult = favoriteService.getUserFavoriteList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 当前用户参与活动列表
     *
     * @param pageForm
     * @return
     */
    @RequireLogin(allow = UserType.GENERAL)
    @GetMapping("my-favorite-activities")
    @ApiDoc
    public ApiResult<PageResult<ActivityVo>> getFavoriteActivityList(ActivityListForm pageForm){
        if(pageForm.getStatus() != null){
            PageResult pageResult = favoriteService.getUserActivityListByStatus(curUser().getId(), pageForm);
            return ApiResult.ok(pageResult);
        }
        PageResult pageResult = favoriteService.getUserActivityList(curUser().getId(), pageForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取百科订阅数
     * @param companyId
     * @return
     */
    @ApiDoc
    @GetMapping("wiki-favorite-count")
    public ApiResult<Integer> getWikiFavoriteCount(@RequestParam Long companyId){
        Integer count = favoriteService.getWikiFavoriteCount(companyId);
        return ApiResult.ok(count);
    }
}
