package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.worldelite.job.entity.Registration;
import lombok.Data;

import java.util.List;

/**
 * 活动报名视图
 */
@Data
public class RegistrationVo implements VoConvertable<RegistrationVo, Registration>{

    private Integer id; //报名ID
    private Integer activityId; //活动ID
    private Integer number; //活动编号
    private String name; //姓名
    private String gender; //性别
    private String phone; //手机号
    private String email; //邮箱
    private String school; //学校
    private String grade; //年级
    private String profession; //专业
    private String education; //学历
    private Integer status; //审核状态

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long resumeId; //简历ID

    private List<QuestionnaireAnswerVo> answerList; //回答列表

    @Override
    public RegistrationVo asVo(Registration registration) {
        BeanUtil.copyProperties(registration,this);
        return this;
    }
}
