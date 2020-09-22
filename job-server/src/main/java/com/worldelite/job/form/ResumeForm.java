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

    private Long id; //简历ID
    @NotBlank(message = "{edit.resume.name.not.blank}")
    private String name; //名称
    @NotNull(message = "{edit.resume.birth.not.null}")
    private Byte gender; //性别
    @NotNull(message = "{edit.resume.gender.not.null}")
    @JSONField(format = "yyyy-MM-dd")
    private Date birth; //生日
    private Integer countryId; //国籍
    private Integer maxDegreeId; //最高学历
    @JSONField(format = "yyyy-MM")
    private Date returnTime; //回国时间
    @NotNull(message = "{edit.resume.graduate.time.not.null}")
    @JSONField(format = "yyyy-MM")
    private Date graduateTime; //毕业时间
    @NotBlank(message = "{edit.resume.place.not.blank}")
    private String curPlace; //现居地址
    private String introduction; //个人接受
    private String phoneCode; //电话区号
    private Long phone; //电话号码
    private String email; //邮箱
    private String avatar; //头像
    private String attachResume; //附件简历

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
