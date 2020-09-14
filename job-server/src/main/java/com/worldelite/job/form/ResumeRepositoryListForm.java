package com.worldelite.job.form;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.codec.digest.DigestUtils;

import javax.validation.constraints.NotBlank;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeRepositoryListForm extends PageForm implements IExportable{
    private Long id; //简历ID
    @NotBlank(message = "{edit.resume.name.not.blank}")
    private String name; //名称
    private String birth; //生日
    private String phone; //电话号码
    private String email; //邮箱
    private String attachResume; //附件路径
    private String introduction; //自我介绍
    private String keyword; //全文检索关键字
    @Override
    public String genExportExcelName() {
        return "简历库列表_"+DigestUtils.md5Hex("ResumeRepositoryList" + JSON.toJSONString(this)) + ".xlsx";
    }
}
