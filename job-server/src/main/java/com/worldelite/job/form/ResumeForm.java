package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.util.FormUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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

    public String getName() {
        return FormUtils.removeAllHtmlTag(StringUtils.trim(name));
    }

    public String getCurPlace() {
        return FormUtils.removeAllHtmlTag(StringUtils.trim(curPlace));
    }

    public String getIntroduction() {
        return FormUtils.safeHtml(introduction);
    }

    public String getPhoneCode() {
        return FormUtils.removeAllHtmlTag(phoneCode);
    }

    public String getAvatar() {
        return FormUtils.removeAllHtmlTag(avatar);
    }
}
