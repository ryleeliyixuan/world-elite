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
    private Long userId; //用户ID
    private String name; //用户名
    private Byte gender; //性别
    private Integer minAge; //最小年龄区间
    private Integer maxAge; //最大年龄区间
    private Integer[] degreeIds; //学历
    private Integer[] schoolIds; //学校
    private Integer[] categoryIds; //意向职位类型
    private Integer[] cityIds; //意向城市
    private Double minGpa; //最小gpa区间
    private Double maxGpa; //最大gpa期间
    private Integer gpaRangeId; //gpa范围ID
    private Integer minSalary; //最低薪酬区间
    private Integer maxSalary; //最高薪酬区间
    private Integer salaryRangeId; //薪酬区间ID

    @Override
    public String genExportExcelName() {
        return "简历列表_"+DigestUtils.md5Hex("ResumeList" + JSON.toJSONString(this)) + ".xlsx";
    }
}
