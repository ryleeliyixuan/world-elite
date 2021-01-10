package com.worldelite.job.util;

import java.io.File;

/**
 * @Author twz
 * @Date 2021-01-10
 * @Desc TODO
 */
public class SimpleFileUtils {

    /**
     * file所在的目录不存在时，为其创建目录
     *
     * @param file
     */
    public static void createDir(File file) {
        if ((file.getAbsolutePath().split("/").length >= 3 || file.getAbsolutePath().split("\\\\").length >= 3)
                && !file.getParentFile().exists()) { // 存在目录
            file.getParentFile().mkdirs(); // 级联创建目录
        }
    }


}
