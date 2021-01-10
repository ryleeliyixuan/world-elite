package com.worldelite.job.service;

import com.worldelite.job.util.SimpleFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
@Slf4j
public class FileService {

    @Value("${temp.path}")
    private String tempPath;

    /**
     * 获取文件
     *
     * @param relativePath
     * @return
     */
    public File getFile(String relativePath){
        File file = new File(tempPath, relativePath);
        SimpleFileUtils.createDir(file);
        return file;
    }

    /**
     * 删除文件或者目录
     *
     * @param file
     */
    public void deleteFile(String file){
        try{
            FileUtils.forceDelete(getFile(file));
        }catch (IOException ex){
            log.error("delete file error: " + file, ex);
        }
    }

    /**
     * 清理临时文件目录
     *
     * @param before 在这个时间之前的文件
     */
    public void clearCacheFiles(Date before){
        try{
            File tempDir = new File(tempPath);
            File[] childFiles = tempDir.listFiles();
            if(childFiles != null && childFiles.length != 0){
                for(File file : childFiles){
                    if(file.lastModified() < before.getTime()){
                        FileUtils.forceDelete(file);
                    }
                }
            }
        }catch (IOException ex){
            log.error("clearCacheFiles error ", ex);
        }
    }

    /**
     * 获取文件相对路径
     *
     * @param f
     * @return
     */
    public String getFileKey(File f){
        return f.getPath().replace(tempPath,"");
    }
}
