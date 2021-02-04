package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * 导出报名表PDF文件视图
 */
@Data
public class RegistrationPDFVo {

    private String title; //报名表标题
    private String nameFlag; //姓名标志
    private String genderFlag; //性别标志
    private String phoneFlag; //手机号标志
    private String emailFlag; //邮箱标志
    private String schoolFlag; //学校标志
    private String gradeFlag; //年级标志
    private String professionFlag; //专业标志
    private String educationFlag; //学历标志

    private Integer id; //报名ID
    private Integer activityId; //活动ID
    private String number; //活动编号
    private String name; //姓名
    private String gender; //性别
    private String phone; //手机号
    private String email; //邮箱
    private String school; //学校
    private String grade; //年级
    private String profession; //专业
    private DictVo education; //学历

    private List<QuestionnaireWithAnswerVo> questionnaireList; //问卷列表

}
