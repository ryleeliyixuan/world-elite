package com.worldelite.job.service.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
class ResumeExcel {
    @ExcelProperty("简历ID")
    private String resumeId;
    @ExcelProperty("名字")
    private String name;
    @ExcelProperty("性别")
    private String gender;
    @ExcelProperty("年龄")
    private Integer age;
    @ExcelProperty("学校")
    private String school;
    @ExcelProperty("专业")
    private String major;
    @ExcelProperty("学历")
    private String degree;
    @ExcelProperty("GPA")
    private String gpa;
    @ExcelProperty("回国时间")
    private String returnTime;
    @ExcelProperty("毕业时间")
    private String graduateTime;
    @ExcelProperty("现居城市")
    private String curPlace;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("投递总数")
    private Integer totalResumeCount;
    @ExcelProperty("投递中")
    private Integer applyingCount;
    @ExcelProperty("进入初筛")
    private Integer applyCandidateCount;
    @ExcelProperty("进入面试")
    private Integer applyInterviewCount;
    @ExcelProperty("已录用")
    private Integer applyOfferCount;
}
