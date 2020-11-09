package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyRecruitForm {
	private Integer id;
    private Long companyId;
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date time; //时间点
    private String event; //进度事件
}
