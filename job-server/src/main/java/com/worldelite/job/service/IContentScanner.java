package com.worldelite.job.service;

import com.worldelite.job.entity.ScanResult;

/**
 * 内容检测
 *
 * @author yeguozhong yedaxia.github.com
 */
public interface IContentScanner {

    /**
     * 检测文本
     *
     * @param content
     */
    ScanResult scanText(String content) throws Exception;
}
