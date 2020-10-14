package com.worldelite.job.service.search;

import com.worldelite.job.entity.ResumeDetail;
import org.apache.lucene.document.Document;

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
     * @return
     */
    Document saveJobItem(Long jobId);

    Document saveJobItem(Document document);

    /**
     * 在索引中删除职位记录
     *
     * @param jobId
     * @return
     */
    Document deleteJobItem(Long jobId);

    Document deleteJobItem(Document document);

    /**
     * 在索引中添加或者更新简历索引
     *
     * @param resumeId
     */
    void saveResumeItem(Long resumeId);
    void saveResumeItem(ResumeDetail resumeDetail,String indexPath);

    /**
     * 在索引中删除简历记录
     * @param resumeId
     */
    void deleteResumeItem(Long resumeId);
}
