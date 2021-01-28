package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ConfigCarousel;
import lombok.Data;

import java.util.Date;

/**
 * 首页轮播图视图
 */
@Data
public class ConfigCarouselVo implements VoConvertable<ConfigCarouselVo, ConfigCarousel>{

    private Integer id; //ID
    private String name; //图片名
    private String url; //图片路径
    private String target; //跳转链接
    private Integer position; //排序
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime; //更新时间

    @Override
    public ConfigCarouselVo asVo(ConfigCarousel configCarousel) {
        if(configCarousel == null){
            return null;
        }
        BeanUtil.copyProperties(configCarousel,this);
        return this;
    }
}
