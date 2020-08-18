package com.worldelite.job.service.read.impl;

import com.worldelite.job.constants.FileType;
import com.worldelite.job.service.read.ResumeFileRead;
import com.worldelite.job.util.FileDownloadUtil;
import com.worldelite.job.util.FileTypeJudge;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.Objects;

/**
 * word简历读取
 **/
@Component
@Slf4j
public class ResumeWordFileRead implements ResumeFileRead {

    @Override
    public String read(String resumePath) throws IOException {
        return read(new File(resumePath));
    }

    @Override
    public String read(File resumeFile) throws IOException {
        InputStream stream = new FileInputStream(resumeFile);
        return read(stream);
    }

    @Override
    public String read(URL resumeUrl) throws IOException {
        InputStream is = FileDownloadUtil.syncDownload(resumeUrl);
        return read(is);
    }

    @Override
    public String read(InputStream resumeInputStream) throws IOException {
        return getTextFromWord(resumeInputStream);
    }

    /**
     * 读取word简历内容
     *
     * @param inputStream 简历文件输入流
     * @return 简历内容文本
     */
    private String getTextFromWord(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        FileType fileType = FileTypeJudge.getType(bufferedInputStream);
        String txtContent = "";
        switch (Objects.requireNonNull(fileType)) {
            case DOC:

                try (HWPFDocument document = new HWPFDocument(bufferedInputStream);
                     WordExtractor docExtractor = new WordExtractor(document)) {
                    txtContent = docExtractor.getText();
                } finally {
                    Objects.requireNonNull(bufferedInputStream).close();
                    Objects.requireNonNull(inputStream).close();
                }
                break;

            case DOCX:

                try (XWPFDocument doc = new XWPFDocument(bufferedInputStream);
                     XWPFWordExtractor docxExtractor = new XWPFWordExtractor(doc)) {
                    txtContent = docxExtractor.getText();
                } finally {
                    Objects.requireNonNull(bufferedInputStream).close();
                    Objects.requireNonNull(inputStream).close();
                }
                break;

            default:
                log.warn("不是有效的Word文件");
        }

        return txtContent;
    }
}
