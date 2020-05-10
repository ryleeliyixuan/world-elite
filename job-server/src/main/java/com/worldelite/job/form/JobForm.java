package com.worldelite.job.form;

import com.worldelite.job.util.FormUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class JobForm {
    private Long id;
    @NotBlank(message = "{edit.job.name.not.blank}")
    private String name;
    @NotNull(message = "{edit.job.category.not.null}")
    private Integer categoryId;
    private String depart;
    @NotNull(message = "{edit.job.degree.not.null}")
    private Integer minDegreeId;
    @NotNull(message = "{edit.job.salary.not.null}")
    private Integer minSalary;
    @NotNull(message = "{edit.job.salary.not.null}")
    private Integer maxSalary;
    private Integer salaryMonths;
    @NotNull(message = "{edit.job.address.not.null}")
    private String address;
    @NotNull(message = "{edit.job.type.not.null}")
    private Integer jobType;
    @NotNull(message = "{edit.job.description.not.blank}")
    private String description;
    @NotNull(message = "{edit.job.city.not.blank}")
    private Integer cityId;

    public String getName() {
        return FormUtils.removeAllHtmlTag(name);
    }

    public String getDepart() {
        return FormUtils.removeAllHtmlTag(depart);
    }

    public String getDescription() {
        return FormUtils.safeHtml(description);
    }
}
