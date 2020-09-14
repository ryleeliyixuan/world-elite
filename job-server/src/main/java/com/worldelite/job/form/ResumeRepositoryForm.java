package com.worldelite.job.form;

import com.worldelite.job.util.FormUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 简历库表单
 */
@Data
public class ResumeRepositoryForm {

    private Long id; //简历ID
    @NotBlank(message = "{edit.resume.name.not.blank}")
    private String name; //名称
    private String birth; //生日
    private String phone; //电话号码
    private String email; //邮箱
    private String attachResume; //附件路径
    private String introduction; //自我介绍

    public String getName() {
        return FormUtils.removeAllHtmlTag(StringUtils.trim(name));
    }

    public String getIntroduction() {
        return FormUtils.safeHtml(introduction);
    }
}
