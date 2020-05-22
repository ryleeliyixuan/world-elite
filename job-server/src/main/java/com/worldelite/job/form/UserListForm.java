package com.worldelite.job.form;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserListForm extends PageForm implements IExportable{

    private Long id; //用户ID
    private String name; //用户名
    private String email; //邮箱
    private Byte type; //用户类型
    private Byte status; //用户状态
    private Long companyId; //公司ID
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime; //开始时间区间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;  //结束时间区间
    private Byte subscribeFlag; //订阅标志


    @Override
    public String genExportExcelName() {
        return "用户列表_"+DigestUtils.md5Hex("UserList" + JSON.toJSONString(this)) + ".xlsx";
    }
}
