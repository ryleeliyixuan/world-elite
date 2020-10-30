package com.worldelite.job.mq;

import com.worldelite.job.constants.BusinessType;
import com.worldelite.job.constants.OperationType;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.service.ResumeAttachService;
import com.worldelite.job.service.resume.ResumeService;
import com.worldelite.job.service.resume.ResumeServiceFactory;
import com.worldelite.job.service.search.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * lucene index cmd 消费
 **/
@Slf4j
@Component
public class LuceneCmdHandler {

    @Autowired
    private ResumeAttachService resumeAttachService;

    @Autowired
    private ResumeServiceFactory resumeServiceFactory;

    private IndexService indexService;

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
                resumeAttachService.deleteIndex(message.getDoc());
            } else if (message.getOpType() == OperationType.CreateOrUpdate) {
                resumeAttachService.updateIndex(message.getDoc());
            }
        } else if (message.getBizType() == BusinessType.Job) {
            if (message.getOpType() == OperationType.Delete) {
                indexService.deleteJobItem(message.getDoc());
            } else if (message.getOpType() == OperationType.CreateOrUpdate) {
                indexService.saveJobItem(message.getDoc());
            }
        }else if (message.getBizType() == BusinessType.Resume) {
            if(message.getOpType() == OperationType.CreateOrUpdate) {
                ResumeService resumeService = resumeServiceFactory.getDefaultService();
                resumeService.saveResumeItem(message.getDoc());
            }
        }
    }
}
