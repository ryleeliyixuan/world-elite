package com.worldelite.job.mq;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.context.MessageTopic;
import com.worldelite.job.form.JobListForm;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.form.UserListForm;
import com.worldelite.job.service.excel.IExportExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 导出excel任务消息处理
 *
 * @author yeguozhong yedaxia.github.com
 */
@Component
@Slf4j
public class ExportTaskHandler implements MessageListener {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IExportExcelService exportExcelService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<String> valueSerializer = stringRedisTemplate.getStringSerializer();
        String deserialize = valueSerializer.deserialize(message.getBody());
        ExportMessage exportMessage = JSON.parseObject(deserialize, ExportMessage.class);
        if (exportMessage != null) {
            switch (exportMessage.getMsgType()) {
                case ExportMessageType.EXPORT_USER:
                    handleExportUserList(exportMessage);
                    break;
                case ExportMessageType.EXPORT_RESUME:
                    handleExportResumeList(exportMessage);
                    break;
                case ExportMessageType.EXPORT_JOB:
                    handleExportJobList(exportMessage);
                    break;
            }
        }
    }

    /**
     * 发送消息导出 excel 消息
     *
     * @param message
     */
    public void sendExportMessage(ExportMessage message) {
        stringRedisTemplate.convertAndSend(MessageTopic.TOPIC_EXPORT_EXCEL, JSON.toJSONString(message));
    }

    /**
     * 处理导出业绩统计
     *
     * @param message
     */
    private void handleExportUserList(ExportMessage message) {
        UserListForm listForm = JSON.parseObject(message.getContent(), UserListForm.class);
        exportExcelService.exportUserList(message.getUserId(), listForm);
    }

    /**
     * 导出简历列表
     *
     * @param message
     */
    private void handleExportResumeList(ExportMessage message) {
        ResumeListForm listForm = JSON.parseObject(message.getContent(), ResumeListForm.class);
        exportExcelService.exportResumeList(message.getUserId(), listForm);
    }

    /**
     * 导出职位列表
     *
     * @param message
     */
    private void handleExportJobList(ExportMessage message){
        JobListForm jobListForm = JSON.parseObject(message.getContent(), JobListForm.class);
        exportExcelService.exportJobList(message.getUserId(), jobListForm);
    }
}
