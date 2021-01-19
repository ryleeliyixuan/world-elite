package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.worldelite.job.entity.OrganizerCredit;
import lombok.Data;

/**
 * @author Xiang Chao
 **/
@Data
public class OrganizerCreditVo implements VoConvertable<OrganizerCreditVo, OrganizerCredit> {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId; //主办方用户id
    private String organizerName; //组织名称
    private String organizerType; //举办方类型; 1:校园组织;2:社会组织;3:个人;4:企业
    private Byte credit; //信用等级; 1:一级;2:二级;3:三级

    private Integer totalActivity; //总活动数
    private Integer passedActivity;//通过活动数
    private Integer notPassActivity;//未通过活动数
    private Float holdRate;//举办率; 状态为已结束/活动总数
    private Integer reportActivity;//被举报活动数
    private Float reportRate;//举报率; 被举报活动数/活动总数

    @Override
    public OrganizerCreditVo asVo(OrganizerCredit organizerCredit) {
        if (organizerCredit == null) return null;
        BeanUtil.copyProperties(organizerCredit, this);

        return this;
    }
}
