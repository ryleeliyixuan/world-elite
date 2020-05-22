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
    private Long id; //职位ID
    @NotBlank(message = "{edit.job.name.not.blank}")
    private String name; //职位名称
    @NotNull(message = "{edit.job.category.not.null}")
    private Integer categoryId; //类型ID
    private String depart; //部门
    @NotNull(message = "{edit.job.degree.not.null}")
    private Integer minDegreeId; // 学历要求
    @NotNull(message = "{edit.job.salary.not.null}")
    private Integer minSalary; //最低薪资
    @NotNull(message = "{edit.job.salary.not.null}")
    private Integer maxSalary; //最高薪资
    private Integer salaryMonths; //发放月数
    @NotNull(message = "{edit.job.address.not.null}")
    private String address; //工作地址
    @NotNull(message = "{edit.job.type.not.null}")
    private Integer jobType; //职位类型
    @NotNull(message = "{edit.job.description.not.blank}")
    private String description; //职位描述
    @NotNull(message = "{edit.job.city.not.blank}")
    private Integer cityId; //所在城市

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
