package com.worldelite.job.form;

import com.worldelite.job.util.FormUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyForm {
    private Long id; //公司ID
    @NotBlank(message = "{edit.company.name.not.blank}")
    private String name; //公司简称
    private String fullName; //公司全称
    private String logo; //logo
    @NotNull(message = "{edit.company.scale.not.null}")
    private Integer scaleId; //规模ID
    @NotNull(message = "{edit.company.stage.not.null}")
    private Integer stageId; //阶段ID
    @NotNull(message = "{edit.company.industry.not.null}")
    private Integer industryId; //行业ID
    @NotNull(message = "{edit.company.property.not.null}")
    private Integer propertyId; //性质ID
    private String homepage; //主页
    private String introduction; //公司介绍
    private String synopsis; //一句话描述

    private String video; //公司视频
    private Integer cityId; //地址ID



    public void setName(String name) {
        this.name = FormUtils.removeAllHtmlTag(name);
    }

    public void setFullName(String fullName) {
        this.fullName = FormUtils.removeAllHtmlTag(fullName);
    }

    public void setIntroduction(String introduction) {
        this.introduction = FormUtils.safeHtml(introduction);
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = FormUtils.removeAllHtmlTag(synopsis);
    }
}
