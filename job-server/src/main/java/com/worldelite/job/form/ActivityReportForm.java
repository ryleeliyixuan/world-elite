package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author Xiang Chao
 **/
@Data
public class ActivityReportForm extends PageForm {
    private Integer id; //举报ID
    private Integer activityId; //活动ID
    private Long reportUserId; //举报者用户ID,留空为当前登录账户
    private Integer optionId; //举报理由选项Dict
    private String reason; //举报原因
    private String[] evidence; //证据图片url
    private Byte status; //举报状态, 0:审核中 1:驳回举报,2:下架活动. 管理端更新用
    private String result; //举报处理结果

    public String getEvidence() {
        return StringUtils.join(evidence, ";");
    }
}
