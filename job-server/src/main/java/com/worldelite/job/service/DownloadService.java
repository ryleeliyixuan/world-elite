package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.entity.Download;
import com.worldelite.job.form.DownloadListForm;
import com.worldelite.job.mapper.DownloadMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 下载管理
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class DownloadService {

    @Autowired
    private DownloadMapper downloadMapper;

    /**
     * 保存下载
     * @param download
     */
    public void saveDownload(Download download){
        if(download.getId() == null){
            downloadMapper.insertSelective(download);
        }else{
            download.setUpdateTime(new Date());
            downloadMapper.updateByPrimaryKeySelective(download);
        }
    }

    /**
     * 获取下载列表
     *
     * @param listForm
     * @return
     */
    public PageResult<Download> getDownloadList(DownloadListForm listForm){
        Download options = new Download();
        options.setUserId(listForm.getUserId());
        AppUtils.setPage(listForm);
        Page<Download> downloadPage = (Page<Download>) downloadMapper.selectAndList(options);
        PageResult<Download> pageResult = new PageResult<>(downloadPage);
        pageResult.setList(downloadPage);
        return pageResult;
    }

    public Download getDownloadByUrl(String url){
        return downloadMapper.selectByUrl(url);
    }

    /**
     * 清理下载记录
     *
     * @param createTime
     */
    public void clearDownloadRecords(Date createTime){
        downloadMapper.deleteLessThanCreateTime(createTime);
    }
}
