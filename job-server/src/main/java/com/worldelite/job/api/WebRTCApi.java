package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.entity.ChannelUser;
import com.worldelite.job.form.ChannelUserForm;
import com.worldelite.job.form.RTCTokenForm;
import com.worldelite.job.service.ChannelUserService;
import com.worldelite.job.service.sdk.AliRTCTokenService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.ChannelUserVo;
import com.worldelite.job.vo.RTCTokenVo;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web视频接口
 *
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/webrtc/")
public class WebRTCApi {

    @Autowired
    private AliRTCTokenService aliRTCTokenService;

    @Autowired
    private ChannelUserService channelUserService;

    /**
     * 获取token
     * @param rtcTokenForm
     * @return
     */
    @ApiDoc
    @PostMapping("token")
    @RequireLogin
    public ApiResult<RTCTokenVo> getWebRTCToken(@RequestBody RTCTokenForm rtcTokenForm){
        RTCTokenVo token = aliRTCTokenService.getToken(rtcTokenForm);
        return ApiResult.ok(token);
    }

    /**
     * 加入或创建房间
     * @param channelUserForm
     * @return
     */
    @ApiDoc
    @PostMapping("save-channel")
    @RequireLogin
    public ApiResult<ChannelUserVo> saveChannelUser(@RequestBody ChannelUserForm channelUserForm){
        ChannelUser channelUser = channelUserService.save(channelUserForm);
        return ApiResult.ok(new ChannelUserVo().asVo(channelUser));
    }

    /**
     * 查询房间
     * @return
     */
    @ApiDoc
    @PostMapping("list-channel")
    @RequireLogin
    public ApiResult<List<ChannelUserVo>> listChannelUser(){
        List<ChannelUserVo> channelUserList = channelUserService.listChannelUserVo();
        return ApiResult.ok(channelUserList);
    }

    /**
     * 删除房间
     * @param id
     * @return
     */
    @ApiDoc
    @PostMapping("delete-channel")
    @RequireLogin
    public ApiResult deleteChannelUser(@RequestParam Integer id){
        channelUserService.delete(id);
        return ApiResult.ok();
    }
}
