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

    private Integer id;
    private String title;
    private String thumbnail;
    private String summary;
    private String url;
    private String description;
    private DictVo city;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date finishTime;
    private String address;

    private Boolean joinFlag;

    @Override
    public ActivityVo asVo(Activity activity) {
        BeanUtil.copyProperties(activity, this);
        setThumbnail(AppUtils.absOssUrl(activity.getThumbnail()));
        return this;
    }
}
