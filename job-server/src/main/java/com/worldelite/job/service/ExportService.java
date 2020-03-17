package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.context.config.DomainConfig;
import com.worldelite.job.entity.Download;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.IExportable;
import com.worldelite.job.form.UserListForm;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.mq.ExportMessage;
import com.worldelite.job.mq.ExportTaskHandler;
import com.worldelite.job.service.excel.IExportExcelService;
import com.worldelite.job.vo.ApiCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@Service
public class ExportService extends BaseService {

    @Autowired
    private FileService fileService;

    @Autowired
    private DomainConfig domainConfig;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ExportTaskHandler exportTaskHandler;

    /**
     * 导出PDF简历
     *
     * @param resumeId
     * @return
     */
    public String exportResumeToPdf(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        if(resume == null){
            throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
        }
        final String resumeTplUrl = String.format("%s/resume/%s?_token=%s",
                domainConfig.getLocalHost(), resumeId, curUser().getToken());
        try {
            File resumePdfFile = fileService.getFile(UUID.randomUUID().toString() + ".pdf");
            Runtime.getRuntime().exec(String.format("wkhtmltopdf %s %s", resumeTplUrl, resumePdfFile.getAbsolutePath()));
            return resumePdfFile.getName();
        } catch (Exception ex) {
            log.error("导出简历失败:" + resumeId, ex);
            throw new ServiceException(message("export.resume.fail"));
        }
    }

    /**
     * 把导出excel任务放进队列
     *
     * @param messageType
     * @param exportForm
     */
    public void queueExportExcel(Byte messageType, IExportable exportForm){
        final String excelKey = exportForm.genExportExcelName();
        final String lockFlag = curUser().getId() + excelKey;
        if(stringRedisTemplate.opsForValue().get(lockFlag) != null){
            throw new ServiceException(message("export.excel.task.running"));
        }
        stringRedisTemplate.opsForValue().set(lockFlag, "true", 1, TimeUnit.MINUTES);
        Download download = downloadService.getDownloadByUrl(excelKey);
        if(download != null){
            throw new ServiceException(message("export.excel.task.exists"));
        }
        ExportMessage exportMessage = new ExportMessage(messageType, curUser().getId(), JSON.toJSONString(exportForm));
        exportTaskHandler.sendExportMessage(exportMessage);
    }

}
