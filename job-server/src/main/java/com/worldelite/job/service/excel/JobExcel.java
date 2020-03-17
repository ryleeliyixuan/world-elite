package com.worldelite.job.service.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.worldelite.job.vo.DictVo;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class JobExcel {
    @ExcelProperty("职位ID")
    private String id;
    @ExcelProperty("职位名称")
    private String name;
    @ExcelProperty("职位分类")
    private String category;
    @ExcelProperty("职位类型")
    private String jobType;
    @ExcelProperty("发布者")
    private String creator;
    @ExcelProperty("公司")
    private String company;
    @ExcelProperty("薪资待遇")
    private String salary;
    @ExcelProperty("学历要求")
    private String degree;
    @ExcelProperty("城市")
    private String city;
    @ExcelProperty("投递人数")
    private Integer totalResumeCount;
    @ExcelProperty("待处理")
    private Integer newResumeCount;
    @ExcelProperty("候选简历")
    private Integer candidateResumeCount;
    @ExcelProperty("进入面试")
    private Integer interviewResumeCount;
}
