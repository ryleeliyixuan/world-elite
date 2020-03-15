package com.worldelite.job.form;

import com.worldelite.job.util.FormUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "{edit.company.scale.not.null}")
    private Integer scaleId;
    @NotBlank(message = "{edit.company.stage.not.null}")
    private Integer stageId;
    @NotBlank(message = "{edit.company.industry.not.null}")
    private Integer industryId;
    @NotBlank(message = "{edit.company.property.not.null}")
    private Integer propertyId;
    private String homepage;
    private String introduction;

    public String getName() {
        return FormUtils.removeAllHtmlTag(name);
    }

    public String getFullName() {
        return FormUtils.removeAllHtmlTag(fullName);
    }

    public String getHomepage() {
        return FormUtils.removeAllHtmlTag(homepage);
    }

    public String getIntroduction() {
        return FormUtils.safeHtml(introduction);
    }
}
