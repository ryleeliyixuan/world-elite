package com.worldelite.job.mq;

import com.worldelite.job.constants.BusinessType;
import com.worldelite.job.constants.OperationType;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * lucene index cmd 消费
 **/
@Slf4j
@Component
public class LuceneCmdHandler {

    /**
     * 添加或更新附件简历
     *
     * @param message
     */
    @RabbitListener(queues = "#{luceneIndexCmdQueue.name}", containerFactory = "customContainerFactory")
    public void processLuceneAddOrUpdate(LuceneIndexCmdDto message) {
        log.info(String.valueOf(message));

        //TODO 调用对应的service操作索引
        if (message.getBizType() == BusinessType.AttachResume) {
            if (message.getOpType() == OperationType.Delete) {

            } else if (message.getOpType() == OperationType.CreateOrUpdate) {

            }
        } else if (message.getBizType() == BusinessType.Job) {

        }
    }
}
