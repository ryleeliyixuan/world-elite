package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ModifyEmailForm {
    @NotBlank(message = "{password.not.blank}")
    private String password; //密码
    @Email(message = "{email.format.error}")
    private String newEmail; //新邮箱
    @NotBlank(message = "{validCode.not.blank}")
    private String validCode; //新邮箱验证码
    @NotBlank(message = "{imageValidCode.not.blank}")
    private String imgValidCode; //图片验证码
}
