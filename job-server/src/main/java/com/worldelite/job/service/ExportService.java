package com.worldelite.job.service;

import com.worldelite.job.context.config.DomainConfig;
import com.worldelite.job.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

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

    /**
     * 导出PDF简历
     *
     * @param resumeId
     * @return
     */
    public String exportResumeToPdf(Long resumeId) {
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

}
