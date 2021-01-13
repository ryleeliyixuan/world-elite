package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.util.FormUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeForm {

    public Long getId() {
        return id;
    }

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

    private Byte maritalStatus; // 政治面貌

    public String getEmail() {
        return email;
    }

    private String email; //邮箱
    private String avatar; //头像

    private Byte priority;

    public List<AttachOtherForm> getAttachOthers() {
        return attachOthers;
    }

    private List<AttachOtherForm> attachOthers;

    public String getAttachResume() {
        return attachResume;
    }

    public void setAttachResume(String attachResume) {
        this.attachResume = attachResume;
    }

    private String attachResume;

    private Long companyId; //公司ID
    private Byte status; //简历状态
    private Byte type; //简历类型

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
