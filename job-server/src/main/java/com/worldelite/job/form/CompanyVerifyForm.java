package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyVerifyForm {
    @NotBlank(message = "{form.verify.name.not.blank}")
    private String name;
    @NotBlank(message = "{form.verify.idCard.not.blank}")
    private String idCard;
    @NotBlank(message = "{form.verify.idCardPic.not.blank}")
    private String idCardPic;
    @NotBlank(message = "{form.verify.company.not.blank}")
    private String company;
    @NotBlank(message = "{form.verify.post.not.blank}")
    private String post;
    @NotBlank(message = "{form.verify.companyLicensePic.not.blank}")
    private String companyLicensePic;
}
