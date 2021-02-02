package com.worldelite.job.service.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 报名者列表Excel列
 */
@Data
public class RegistrationExcel {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("性别")
    private String gender;
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("学校")
    private String school;
    @ExcelProperty("年级")
    private String grade;
    @ExcelProperty("专业")
    private String profession;
    @ExcelProperty("学历")
    private String education;
    @ExcelProperty("状态")
    private String status;
}
