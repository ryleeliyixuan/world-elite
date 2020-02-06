package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.validator.constraints.SafeHtml;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeForm {
    private Long id;
    private String name;
    private Byte gender;
    @JSONField(format = "yyyy-MM-dd")
    private Date birth;
    private Integer countryId;
    private Integer maxDegreeId;
    private Byte maritalStatus;
    @JSONField(format = "yyyy-MM")
    private Date returnTime;
    @JSONField(format = "yyyy-MM")
    private Date graduateTime;
    private String curPlace;
    private String introduction;
    private String phoneCode;
    private Long phone;
    private String avatar;
}
