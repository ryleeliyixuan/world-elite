package com.worldelite.job.service.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 简历文件读取接口
 *
 * @author Kaers
 **/
public interface ResumeFileRead {

    String read(String resumePath) throws IOException;

    String read(File resumeFile) throws IOException;

    String read(URL resumeUrl) throws IOException;

    String read(InputStream resumeInputStream) throws IOException;
}
