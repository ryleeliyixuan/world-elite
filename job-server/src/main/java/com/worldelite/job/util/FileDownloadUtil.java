package com.worldelite.job.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/**
 * 使用okhttp3进行文件下载
 **/
@Slf4j
public class FileDownloadUtil {

    /**
     * okhttp3的同步下载方法
     *
     * @param url 文件url
     * @return 文件InputStream
     * @throws IOException 超时/失败
     */
    public static InputStream syncDownload(final URL url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            //由于后续还要对流进行读写操作，此处需要先拿到全部bytes. 如果只是返回一个byteStream(),操作一次后就会被关闭
            return new ByteArrayInputStream(Objects.requireNonNull(response.body()).bytes());
        }
    }
}
