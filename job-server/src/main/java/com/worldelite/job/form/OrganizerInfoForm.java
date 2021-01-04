package com.worldelite.job.form;

import lombok.Data;

/**
 * @author Xiang Chao
 **/
@Data
public class OrganizerInfoForm extends PageForm {
    private Integer id; //自增id
    private Long userId; //主办方用户id
    private String organizerName; //组织名称
    private String school; //所属学校,若是校园组织
    private String businessLicenseUrl; //营业执照,若是社会组织
}
