package com.worldelite.job.service.read.impl;

import com.worldelite.job.service.read.ResumeFileRead;
import com.worldelite.job.util.FileDownloadUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.Objects;

/**
 * pdf格式简历读取
 **/
@Component
public class ResumePdfFileRead implements ResumeFileRead {

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
        return getTextFromPDF(resumeInputStream);
    }

    /**
     * 读取PDF简历内容
     *
     * @param inputStream 简历文件输入流
     * @return 简历内容文本
     */
    private String getTextFromPDF(InputStream inputStream) throws IOException {
        //使用BufferedInputStream以便支持mark/reset
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        //FileType fileType = FileTypeJudge.getType(bufferedInputStream);
        //Assert.isTrue(FileType.PDF == fileType, "不是有效的PDF文件");

        try (PDDocument document = PDDocument.load(bufferedInputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(document);
        } finally {
            Objects.requireNonNull(bufferedInputStream).close();
            Objects.requireNonNull(inputStream).close();
        }
    }
}
