package com.worldelite.job.service.read;

import com.worldelite.job.constants.FileType;
import com.worldelite.job.service.read.impl.ResumePdfFileRead;
import com.worldelite.job.service.read.impl.ResumeWordFileRead;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 简历文件读取工厂
 **/
@Component
@RequiredArgsConstructor
public class ResumeFileReadFactory {
    private final ResumePdfFileRead resumePdfFileRead;
    private final ResumeWordFileRead resumeWordFileRead;

    public ResumeFileRead getFileRead(FileType fileType) throws Exception {
        switch (fileType) {
            case PDF:
                return resumePdfFileRead;
            case DOC:
            case DOCX:
                return resumeWordFileRead;
            default:
                throw new Exception("不支持的文件类型:" + fileType.name);
        }
    }

    public ResumeFileRead getFileRead(String suffix) throws Exception {
        return getFileRead(FileType.valueOf(suffix.toUpperCase()));
    }
}
