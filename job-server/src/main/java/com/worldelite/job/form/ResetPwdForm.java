package com.worldelite.job.form;

import com.worldelite.job.constants.Regexs;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResetPwdForm {
    @Email(message = "{email.format.error}")
    private String email;
    @NotBlank(message = "{validCode.not.blank}")
    private String validCode;
    @Pattern(regexp = Regexs.PASSWORD_REGEX, message = "{password.format.error}")
    private String newPassword;
    @NotBlank(message = "{imageValidCode.not.blank}")
    private String imgValidCode;
}