package com.worldelite.job.form;

import com.worldelite.job.constants.Regexs;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class AdminForm {
    @NotNull
    private String name; //名称
    @NotNull
    @Email(message = "{email.format.error}")
    private String email; //email
    @Pattern(regexp = Regexs.PASSWORD_REGEX, message = "{password.format.error}")
    private String password; //密码
}
