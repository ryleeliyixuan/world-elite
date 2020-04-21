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

    private Long id;
    private String name;
    private String email;
    private Byte type;
    private Byte status;
    private Long companyId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;


    @Override
    public String genExportExcelName() {
        return "用户列表_"+DigestUtils.md5Hex("UserList" + JSON.toJSONString(this)) + ".xlsx";
    }
}
