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

    private Long jobId;
    private Long creatorId;
    private Long companyId;
    private Integer jobType;
    private String name;
    private Integer cityId;
    private Byte status;

    @Override
    public String genExportExcelName() {
        return DigestUtils.md5Hex("JobList" + JSON.toJSONString(this)) + ".xlsx";
    }

}
