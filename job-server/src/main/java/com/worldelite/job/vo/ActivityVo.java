package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ActivityVo implements VoConvertable<ActivityVo, Activity>{

    private Integer id; //活动ID
    private String title; //活动标题
    private String thumbnail; //缩略图
    private String summary;  //摘要
    private String url;  //链接
    private String description; //活动详情
    private DictVo city; //活动城市
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date startTime; //开始时间
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date finishTime; //结束时间
    private String address; //活动地址
    private Byte status; //状态

    private Boolean joinFlag; //是否参加
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date joinTime; //参加时间

    @Override
    public ActivityVo asVo(Activity activity) {
        BeanUtil.copyProperties(activity, this);
        setThumbnail(AppUtils.absOssUrl(activity.getThumbnail()));
        return this;
    }
}
