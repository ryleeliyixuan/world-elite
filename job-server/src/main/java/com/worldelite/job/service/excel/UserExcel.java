package com.worldelite.job.service.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
class UserExcel {
    @ExcelProperty("用户ID")
    private String userId;
    @ExcelProperty("用户名")
    private String name;
    @ExcelProperty("性别")
    private String gender;
    @ExcelProperty("电话区号")
    private String phoneCode;
    @ExcelProperty("电话")
    private String phone;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("注册时间")
    private String createTime;
    @ExcelProperty("状态")
    private String status;
}
