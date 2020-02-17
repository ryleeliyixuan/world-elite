package com.worldelite.job.service.search;

/**
 * 索引管理接口
 *
 * @author yeguozhong yedaxia.github.com
 */
public interface IndexService {

    /**
     * 重新构建索引
     */
    void createOrRefresh();

    /**
     * 在索引中添加或者更新职位记录
     *
     * @param jobId
     */
    void saveJobItem(Long jobId);

    /**
     * 在索引中删除职位记录
     *
     * @param jobId
     */
    void deleteJobItem(Long jobId);
}
