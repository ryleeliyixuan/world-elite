package com.worldelite.job.form;

import com.worldelite.job.util.FormUtils;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyForm {
    private Long id;
    private String name;
    private String fullName;
    private String logo;
    private Integer scaleId;
    private Integer stageId;
    private Integer industryId;
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
