package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeExpForm {
    private Integer id; //工作经验ID
    private Long resumeId; //简历ID
    @JSONField(format = "yyyy-MM")
    private Date startTime; //开始时间
    @JSONField(format = "yyyy-MM")
    private Date finishTime; //结束时间
    private Byte onWork; //是否在职
    @NotBlank(message = "{edit.resume.exp.company.not.blank}")
    private String company; //公司
    @NotBlank(message = "{edit.resume.exp.depart.not.blank}")
    private String depart; //部门
    @NotBlank(message = "{edit.resume.exp.post.not.blank}")
    private String post; //职位
    @NotBlank(message = "{edit.resume.exp.description.not.blank}")
    private String description; //描述
    @NotBlank(message = "{edit.resume.exp.worktype.not.blank}")
    private String workType; // 工作类型
    private String industry;
}
