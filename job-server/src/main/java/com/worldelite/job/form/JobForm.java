package com.worldelite.job.form;

import com.worldelite.job.util.FormUtils;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class JobForm {
    private Long id;
    private String name;
    private Integer categoryId;
    private String depart;
    private Integer minDegreeId;
    private Integer minSalary;
    private Integer maxSalary;
    private Integer salaryMonths;
    private Integer addressId;
    private Integer jobType;
    private String description;

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
