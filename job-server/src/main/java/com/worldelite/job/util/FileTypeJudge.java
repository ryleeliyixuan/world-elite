package com.worldelite.job.util;

import com.worldelite.job.constants.FileType;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件类型判断
 **/
public class FileTypeJudge {

    /**
     * Constructor
     */
    private FileTypeJudge() {
    }

    /**
     * 将文件头转换成16进制字符串
     */
    private static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到文件头
     */
    private static String getFileContent(InputStream inputStream) throws IOException {

        byte[] b = new byte[28];

        if (inputStream.markSupported()) {
            inputStream.mark(28);
            int result = inputStream.read(b, 0, 28);
            Assert.isTrue(result > 0, "无法读取文件头");
            inputStream.reset();
        } else {
            throw new UnsupportedOperationException("暂时仅支持允许mark/reset的inputStream");
        }

        return bytesToHexString(b);
    }

    /**
     * 判断文件类型
     */
    public static FileType getType(InputStream inputStream) throws IOException {

        String fileHead = getFileContent(inputStream);

        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }

        for (FileType type : FileType.values()) {
            if (fileHead.startsWith(type.code)) {
                return type;
            }
        }

        return null;
    }
}
