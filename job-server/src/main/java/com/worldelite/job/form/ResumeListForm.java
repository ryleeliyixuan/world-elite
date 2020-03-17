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
public class ResumeListForm extends PageForm implements IExportable{
    private Long userId;
    private String name;
    private Byte gender;
    private Integer minAge;
    private Integer maxAge;
    private Integer[] degreeIds;
    private Integer[] schoolIds;
    private Integer[] categoryIds;
    private Integer[] cityIds;
    private Double minGpa;
    private Double maxGpa;
    private Integer gpaRangeId;
    private Integer minSalary;
    private Integer maxSalary;
    private Integer salaryRangeId;

    @Override
    public String genExportExcelName() {
        return DigestUtils.md5Hex("ResumeList" + JSON.toJSONString(this)) + ".xlsx";
    }
}
