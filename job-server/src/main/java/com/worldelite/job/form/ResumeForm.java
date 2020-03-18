package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.util.FormUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeForm {

    private Long id;
    @NotBlank(message = "{edit.resume.name.not.blank}")
    private String name;
    @NotNull(message = "{edit.resume.birth.not.null}")
    private Byte gender;
    @NotNull(message = "{edit.resume.gender.not.null}")
    @JSONField(format = "yyyy-MM-dd")
    private Date birth;
    private Integer countryId;
    private Integer maxDegreeId;
    private Byte maritalStatus;
    @JSONField(format = "yyyy-MM")
    private Date returnTime;
    @NotNull(message = "{edit.resume.graduate.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date graduateTime;
    @NotBlank(message = "{edit.resume.place.not.blank}")
    private String curPlace;
    private String introduction;
    private String phoneCode;
    private Long phone;
    private String avatar;
    private String attachResume;

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
