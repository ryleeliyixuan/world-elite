package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = StringUtils.trimToNull(email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = StringUtils.trimToNull(password);
    }
}
