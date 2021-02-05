package com.worldelite.job.form;

import com.worldelite.job.util.FormUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private Integer salaryId; //薪资范围ID
    private Integer salaryMonths; //发放月数
    @NotNull(message = "{edit.job.type.not.null}")
    private Integer jobType; //职位类型
    @NotNull(message = "{edit.job.type.not.null}")
    private Integer recruitType; //招聘类型
    @NotNull(message = "{edit.job.description.not.blank}")
    private String description; //职位描述
    @NotNull(message = "{edit.job.city.not.blank}")
    private Integer cityId; //所在城市
    @NotNull(message = "{edit.job.experience.not.null}")
    private Integer experienceId; //工作经验
    @NotNull(message = "{edit.job.industrytags.not.null}")
    private String[] industryTags; //行业领域
    @NotNull(message = "{edit.job.skilltags.not.null}")
    private String[] skillTags; //技能标签
    private List<Integer> additionIds; //其他要求ID列表
    private String address; //地址
    private Double latitude; //纬度
    private Double longitude; //经度

    public String getName() {
        return FormUtils.removeAllHtmlTag(name);
    }

    public String getDepart() {
        return FormUtils.removeAllHtmlTag(depart);
    }

    public String getDescription() {
        return FormUtils.safeHtml(description);
    }

    public String getIndustryTags(){
        return StringUtils.join(industryTags,',');
    }

    public String getSkillTags(){
        return StringUtils.join(skillTags,',');
    }
}
