package com.worldelite.job.form;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobListForm extends PageForm implements IExportable{

    private Long jobId; //职位ID
    private Long creatorId; //创建人ID
    private Long companyId; //公司ID
    private Integer jobType; //职位类型
    private String name; //名称
    private Integer cityId; //城市ID
    private Byte status; //职位状态

    @Override
    public String genExportExcelName() {
        return DigestUtils.md5Hex("JobList" + JSON.toJSONString(this)) + ".xlsx";
    }

}
