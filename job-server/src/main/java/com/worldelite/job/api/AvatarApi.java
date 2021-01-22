package com.worldelite.job.api;

import com.worldelite.job.form.AvatarForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.AvatarService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.AvatarVo;
import com.worldelite.job.vo.PageResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 头像接口
 *
 * @author Xiang Chao
 **/
@RestController
@RequestMapping("/api/avatar")
@AllArgsConstructor
public class AvatarApi {

    private final AvatarService avatarService;

    @GetMapping
    public ApiResult<PageResult<AvatarVo>> getAvatarList(AvatarForm avatarForm, PageForm pageForm) {
        return ApiResult.ok(avatarService.getAvatarList(avatarForm, pageForm));
    }

    @PostMapping
    public ApiResult<?> addAvatar(@RequestBody AvatarForm form) {
        avatarService.addAvatar(form);
        return ApiResult.ok();
    }

    @PatchMapping
    public ApiResult<?> updateAvatar(@RequestBody AvatarForm form) {
        avatarService.updateAvatar(form);
        return ApiResult.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResult<?> delAvatar(@PathVariable("id") String id) {
        avatarService.delAvatar(id);
        return ApiResult.ok();
    }
}
