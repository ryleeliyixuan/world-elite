package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class LoginForm {
    @Email(message = "{email.format.error}")
    private String email;
    @NotNull(message = "{password.not.empty}")
    private String password;
}
