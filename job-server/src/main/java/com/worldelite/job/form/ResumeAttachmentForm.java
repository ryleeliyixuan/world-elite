package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.util.FormUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 附件简历搜索表单
 */
@EqualsAndHashCode(callSuper=false)
@Data
public class ResumeAttachmentForm extends PageForm{

    private String[] keywords; //附件简历搜索关键字

}
