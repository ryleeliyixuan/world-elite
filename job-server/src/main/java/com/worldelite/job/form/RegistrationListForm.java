package com.worldelite.job.form;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 活动报名查询表单
 */
@Data
public class RegistrationListForm extends PageForm implements IExportable{
    private Integer activityId; //活动ID
    private Long userId; //活动发布者ID
    private Long registrationUserId; //活动报名者ID
    private Integer status; //活动状态
    private String keywords; //关键词

    @Override
    public String genExportExcelName() {
        return DigestUtils.md5Hex("JobList" + JSON.toJSONString(this)) + ".xlsx";
    }
}
