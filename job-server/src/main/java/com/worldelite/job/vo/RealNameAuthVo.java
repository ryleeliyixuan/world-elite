package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.worldelite.job.entity.RealNameAuth;
import lombok.Data;

/**
 * @author Xiang Chao
 **/
@Data
public class RealNameAuthVo implements VoConvertable<RealNameAuthVo, RealNameAuth> {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId; //用户id
    private String name; //姓名
    private String idNumber;//身份证号
    private String faceUrl;//身份证/学生证人像面
    private String emblemUrl;//身份证国徽面/学生证日期面
    private String holdUrl;//手持身份证/学生证正面照
    private Byte status;//审核状态.1:审核中,2:通过,3拒绝
    private Byte businessType;//实名认证业务类型.1:活动
    private String reason; //审核失败原因

    @Override
    public RealNameAuthVo asVo(RealNameAuth realNameAuth) {
        if (realNameAuth == null) return null;
        BeanUtil.copyProperties(realNameAuth, this);

        return this;
    }
}
