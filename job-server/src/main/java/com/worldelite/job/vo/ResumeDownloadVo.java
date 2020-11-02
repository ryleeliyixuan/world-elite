package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.City;
import com.worldelite.job.entity.ResumeDownload;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 简历下载视图对象
 */
@Data
public class ResumeDownloadVo implements VoConvertable<ResumeDownloadVo, ResumeDownload>{

    private Integer id; //下载ID
    private String name; //下载名
    private String fileName; //文件名
    private Integer process; //下载进度
    private Byte status; //下载状态
    private String message; //下载信息
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime; //开始时间

    @Override
    public ResumeDownloadVo asVo(ResumeDownload resumeDownload) {
        BeanUtil.copyProperties(resumeDownload,this);
        return this;
    }
}