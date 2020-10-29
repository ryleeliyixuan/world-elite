package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.UserCorporateComment;
import lombok.Data;

import java.util.Date;

@Data
public class UserCorporateCommentVo implements VoConvertable<UserCorporateCommentVo, UserCorporateComment> {
    private String id; //笔记Id， Primary Key
    private String resumeId; //简历ID
    private String comment; //用户笔记
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间

    @Override
    public UserCorporateCommentVo asVo(UserCorporateComment userCorporateComment) {
        setId(String.valueOf(userCorporateComment.getId()));
        setResumeId(String.valueOf(userCorporateComment.getResumeId()));
        setComment(userCorporateComment.getComment());
        setCreateTime(userCorporateComment.getCreateTime());
        return this;
    }
}


