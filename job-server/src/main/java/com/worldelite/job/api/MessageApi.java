package com.worldelite.job.api;

import com.worldelite.job.anatation.RequireLogin;
import com.worldelite.job.form.MessageListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.service.MessageService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RestController
@RequestMapping("/api/message")
public class MessageApi {

    @Autowired
    private MessageService messageService;

    @RequireLogin
    @GetMapping("list")
    public ApiResult getMessageList(MessageListForm listForm){
        PageResult pageResult = messageService.getUserMessageList(listForm);
        return ApiResult.ok(pageResult);
    }

    @RequireLogin
    @GetMapping("unread-count")
    public ApiResult getUnreadCount(){
        int count = messageService.getUserUnreadCount();
        return ApiResult.ok(count);
    }

    @RequireLogin
    @PostMapping("read-message")
    public ApiResult setMessageRead(@RequestParam Integer id){
        messageService.markMessageRead(id);
        return ApiResult.ok();
    }

    @RequireLogin
    @PostMapping("read-all-message")
    public ApiResult setAllMessageRead(){
        messageService.markAllRead();
        return ApiResult.ok();
    }

    @RequireLogin
    @PostMapping("delete")
    public ApiResult deleteMessage(@RequestParam Integer id){
        messageService.deleteMessage(id);
        return ApiResult.ok();
    }

    @RequireLogin
    @PostMapping("delete-all")
    public ApiResult deleteAllMessage(){
        messageService.deleteAllMessage();
        return ApiResult.ok();
    }
}
