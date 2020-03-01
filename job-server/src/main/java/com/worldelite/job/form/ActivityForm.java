package com.worldelite.job.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ActivityForm {
    private Integer id;
    private String title;
    private String thumbnail;
    private String summary;
    private String url;
    private Integer cityId;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date finishTime;
    private String address;
}
