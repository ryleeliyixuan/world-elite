package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Xiang Chao
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class OrganizerCreditForm extends PageForm {
    private Long userId; //主办方用户id
    private String organizerName; //组织名称
    private String organizerType; //举办方类型; 1:校园组织;2:社会组织;3:个人;4:企业
    private Byte credit; //信用等级; 1:一级;2:二级;3:三级
}
