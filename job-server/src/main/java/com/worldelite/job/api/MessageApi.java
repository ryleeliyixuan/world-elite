package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.MessageListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.MessageService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.MessageVo;
import com.worldelite.job.vo.PageResult;
import io.github.yedaxia.apidocs.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息接口
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/message/")
public class MessageApi {

    @Autowired
    private MessageService messageService;

    /**
     * 当前用户消息列表
     * @param listForm
     * @return
     */
    @RequireLogin
    @GetMapping("list")
    @ApiDoc
    public ApiResult<PageResult<MessageVo>> getMessageList(MessageListForm listForm){
        PageResult pageResult = messageService.getUserMessageList(listForm);
        return ApiResult.ok(pageResult);
    }

    /**
     * 获取未读消息数
     * @return
     */
    @RequireLogin
    @GetMapping("unread-count")
    @ApiDoc
    public ApiResult<Integer> getUnreadCount(){
        int count = messageService.getUserUnreadCount();
        return ApiResult.ok(count);
    }

    /**
     * 设置消息已读
     * @param id 消息id
     * @return
     */
    @RequireLogin
    @PostMapping("read-message")
    @ApiDoc
    public ApiResult setMessageRead(@RequestParam Integer id){
        messageService.markMessageRead(id);
        return ApiResult.ok();
    }

    /**
     * 设置全部消息已读
     * @return
     */
    @RequireLogin
    @PostMapping("read-all-message")
    @ApiDoc
    public ApiResult setAllMessageRead(){
        messageService.markAllRead();
        return ApiResult.ok();
    }

    /**
     * 删除消息
     * @param id 消息ID
     * @return
     */
    @RequireLogin
    @PostMapping("delete")
    @ApiDoc
    public ApiResult deleteMessage(@RequestParam Integer id){
        messageService.deleteMessage(id);
        return ApiResult.ok();
    }

    /**
     * 删除全部消息
     * @return
     */
    @RequireLogin
    @PostMapping("delete-all")
    @ApiDoc
    public ApiResult deleteAllMessage(){
        messageService.deleteAllMessage();
        return ApiResult.ok();
    }
}
