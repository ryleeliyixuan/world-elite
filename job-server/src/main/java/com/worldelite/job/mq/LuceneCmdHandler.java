package com.worldelite.job.mq;

import com.worldelite.job.constants.BusinessType;
import com.worldelite.job.constants.OperationType;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.service.ActivitySearchService;
import com.worldelite.job.service.CompanyNameSearchService;
import com.worldelite.job.service.JobNameSearchService;
import com.worldelite.job.service.ResumeAttachService;
import com.worldelite.job.service.search.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * lucene index cmd 消费
 **/
@Slf4j
@Component
public class LuceneCmdHandler {

    @Autowired
    private ResumeAttachService resumeAttachService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private CompanyNameSearchService companyNameSearchService;

    @Autowired
    private JobNameSearchService jobNameSearchService;

    @Autowired
    private ActivitySearchService activitySearchService;

    @Resource(name = "luceneIndexCmdQueue")
    private Queue queue;

    /**
     * 添加或更新附件简历
     *
     * @param message
     */
    @RabbitListener(queues = "#{luceneIndexCmdQueue.name}", containerFactory = "customContainerFactory")
    public void processLuceneAddOrUpdate(LuceneIndexCmdDto message) {
        log.info("process lucene add or update {}", message);

        if (StringUtils.equals(queue.getActualName(), message.getSource())) {
            log.info("ignore messages sent by itself.");
            return;
        }

        try {
            if (message.getBizType() == BusinessType.RESUME) {
                if (message.getKey() == null || !(message.getKey() instanceof Long)) {
                    log.error("简历索引同步指令无效, 简历ID为空或者不是Long");
                    return;
                }

                Long resumeId = (Long) message.getKey();

                if (message.getOpType() == OperationType.DELETE) {
                    indexService.deleteResumeItem(resumeId);

                } else if (message.getOpType() == OperationType.CREATE_OR_UPDATE) {
                    indexService.saveResumeItem(resumeId);
                }
            }
            //TODO 附件简历部分目前暂时砍掉,代码先留着
            else if (message.getBizType() == BusinessType.ATTACH_RESUME) {
                if (message.getKey() == null || !(message.getKey() instanceof Long)) {
                    log.error("附件简历索引同步指令无效, 简历ID为空或者不是Long");
                    return;
                }

                Long resumeId = (Long) message.getKey();

                //附件简历索引删除
                if (message.getOpType() == OperationType.DELETE) {
                    resumeAttachService.deleteIndex(resumeId);

                }
                //附件简历索引添加或更新
                else if (message.getOpType() == OperationType.CREATE_OR_UPDATE) {
                    resumeAttachService.updateIndex(resumeId);
                }
            } else if (message.getBizType() == BusinessType.JOB) {
                if (message.getKey() == null || !(message.getKey() instanceof Long)) {
                    log.error("职位索引同步指令无效, 职位ID为空或者不是Long");
                    return;
                }

                jobNameSearchService.createOrRefreshJobNameIndex();

                Long jobId = (Long) message.getKey();

                //职位索引删除
                if (message.getOpType() == OperationType.DELETE) {
                    indexService.deleteJobItem(jobId);
                }
                //职位添加或更新
                else if (message.getOpType() == OperationType.CREATE_OR_UPDATE) {
                    indexService.saveJobItem(jobId);
                }
            } else if (message.getBizType() == BusinessType.COMPANY) {
                //公司相关的搜索只有公司名,所以直接全部刷新
                companyNameSearchService.createOrRefreshCompanyNameIndex();
            } else if (message.getBizType() == BusinessType.ACTIVITY) {
                if (message.getKey() == null || !(message.getKey() instanceof Integer)) {
                    log.error("活动索引同步指令无效, 活动ID为空或者不是Integer");
                    return;
                }

                Integer activityId = (Integer) message.getKey();
                activitySearchService.createOrRefreshActivityListIndex(activityId);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
