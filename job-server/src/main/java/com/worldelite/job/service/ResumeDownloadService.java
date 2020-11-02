package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.ResumeDownload;
import com.worldelite.job.form.ResumeDownloadForm;
import com.worldelite.job.form.ResumeDownloadListForm;
import com.worldelite.job.mapper.ResumeDownloadMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeDownloadVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 简历下载服务类
 */
@Slf4j
@Service
public class ResumeDownloadService extends BaseService{

    @Autowired
    private ResumeDownloadMapper resumeDownloadMapper;

    public ResumeDownload save(ResumeDownloadForm resumeDownloadForm){
        ResumeDownload resumeDownload = new ResumeDownload();
        BeanUtil.copyProperties(resumeDownloadForm,resumeDownload);
        if(resumeDownload.getId() == null){
            resumeDownload.setUserId(curUser().getId());
            resumeDownloadMapper.insertSelective(resumeDownload);
            return resumeDownload;
        }
        resumeDownloadMapper.updateByPrimaryKeySelective(resumeDownload);
        return resumeDownload;
    }

    public PageResult<ResumeDownloadVo> list(ResumeDownloadListForm listForm){
        ResumeDownload resumeDownload = new ResumeDownload();
        BeanUtil.copyProperties(listForm,resumeDownload);
        resumeDownload.setUserId(curUser().getId());
        AppUtils.setPage(listForm);
        Page<ResumeDownload> page = (Page<ResumeDownload>)resumeDownloadMapper.selectAndList(resumeDownload);
        PageResult<ResumeDownloadVo>  pageResult = new PageResult<>(page);
        List<ResumeDownloadVo> downloadVoList = new ArrayList<>(page.size());
        for(ResumeDownload download:page){
            downloadVoList.add(toVo(download));
        }
        pageResult.setList(downloadVoList);
        return pageResult;
    }

    public ResumeDownloadVo toVo(ResumeDownload resumeDownload){
        return new ResumeDownloadVo().asVo(resumeDownload);
    }

    public void delete(Integer id){
        resumeDownloadMapper.deleteByPrimaryKey(id);
    }

}
