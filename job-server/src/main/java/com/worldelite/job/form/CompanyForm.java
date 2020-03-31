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
    private Long id;
    @NotBlank(message = "{edit.company.name.not.blank}")
    private String name;
    private String fullName;
    private String logo;
    @NotNull(message = "{edit.company.scale.not.null}")
    private Integer scaleId;
    @NotNull(message = "{edit.company.stage.not.null}")
    private Integer stageId;
    @NotNull(message = "{edit.company.industry.not.null}")
    private Integer industryId;
    @NotNull(message = "{edit.company.property.not.null}")
    private Integer propertyId;
    private String homepage;
    private String introduction;
    private String synopsis;


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
