package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.worldelite.job.entity.OrganizerInfo;
import lombok.Data;

/**
 * @author Xiang Chao
 **/
@Data
public class OrganizerInfoVo implements VoConvertable<OrganizerInfoVo, OrganizerInfo> {
    private Integer id; //自增id
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId; //主办方用户id
    private String organizerName; //组织名称
    private String school; //所属学校,若是校园组织
    private String businessLicenseUrl; //营业执照,若是社会组织

    @Override
    public OrganizerInfoVo asVo(OrganizerInfo organizerInfo) {
        if (organizerInfo == null) return null;
        BeanUtil.copyProperties(organizerInfo, this);

        return this;
    }
}
