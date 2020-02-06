package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class EmailForm {
    private String address; //目标邮箱
    private String subject;//标题
    private String emailBody; //邮箱内容
}
