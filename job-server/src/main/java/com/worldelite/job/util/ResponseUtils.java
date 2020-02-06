package com.worldelite.job.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ResponseUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtils.class);

    public static void writeAsJson(HttpServletResponse response, Object result){
        writeAsString(response, JSON.toJSONString(result));
    }

    public static void writeAsString(HttpServletResponse response, String content){
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            response.getWriter().write(content);
        }catch (Exception ex){
            LOGGER.error("", ex);
        }
    }

    /**
     * 图片返回
     * @param response
     */
    public static void writeAsImage(HttpServletResponse response, BufferedImage image, String format){
        response.setHeader("Transfer-Encoding","chunked");
        response.setContentType("image/"+format);
        try {
            ImageIO.write(image, format, response.getOutputStream());
        }catch (Exception ex){
            LOGGER.error("", ex);
        }
    }

    /**
     * 返回静态文件，不能是大文件
     *
     * @param response
     * @param file
     */
    public static void writeFile(HttpServletResponse response, File file){
        String fileName = file.getName().toLowerCase();
        try{
            writeStream(response, new FileInputStream(file), fileName, file.length());
        }catch (IOException e){
            LOGGER.error("", e);
            writeAsString(response, "error");
        }

    }

    public static void writeStream(HttpServletResponse response, InputStream stream, String fileName, long streamLen){

        String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        String contentType = null;
        switch (fileFormat){
            case "txt":
                contentType = "text/plain;charset=UTF-8";
                break;
            case "html":
                contentType = "text/html;charset=UTF-8";
                break;
            case "pdf":
                contentType = "application/pdf";
                break;
            case "jpg":
            case "jpeg":
            case "png":
                contentType = "image/"+fileFormat;
                break;
            case "zip":
                contentType = "application/x-zip-compressed";
                break;
            case "xlsx":
                contentType = "application/vnd.ms-excel";
                break;
            default:
                contentType = "application/octet-stream";
        }

        if(contentType.startsWith("application")){
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        }

        response.setContentType(contentType);
        response.setContentLengthLong(streamLen);

        try{
            byte[] buffer = new byte[1024];
            int readSize;
            while((readSize = stream.read(buffer)) != -1){
                response.getOutputStream().write(buffer,  0,  readSize);
            }
        }catch (IOException e){
            LOGGER.error("", e);
        }
    }
}
