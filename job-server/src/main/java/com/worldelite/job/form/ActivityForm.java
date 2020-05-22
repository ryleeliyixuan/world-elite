package com.worldelite.job.form;

import com.worldelite.job.util.AppUtils;
import lombok.Data;
import lombok.NonNull;
import me.zhyd.oauth.utils.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ActivityForm {
    private Integer id; //活动ID
    @NotBlank
    private String title; //活动标题
    @NotBlank
    private String thumbnail; // 缩略图
    @NotBlank
    private String summary; //摘要
    @NotNull
    private Integer cityId; //城市ID
    @NotBlank
    private String description; //活动详情
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;  //开始时间
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date finishTime; //结束时间
    @NotBlank
    private String address; //活动地址
}
