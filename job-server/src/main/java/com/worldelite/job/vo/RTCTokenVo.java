package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyComment;
import lombok.Data;

import java.util.Date;

/**
 * 视频Token视图对象
 */
@Data
public class RTCTokenVo{
    private String token;
}
