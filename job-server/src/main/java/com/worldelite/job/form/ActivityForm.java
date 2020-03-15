package com.worldelite.job.form;

import lombok.Data;
import lombok.NonNull;
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
    private Integer id;
    @NotBlank
    private String title;
    @NotBlank
    private String thumbnail;
    @NotBlank
    private String summary;
    @NotBlank
    private String url;
    @NotNull
    private Integer cityId;
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date finishTime;
    @NotBlank
    private String address;
}
