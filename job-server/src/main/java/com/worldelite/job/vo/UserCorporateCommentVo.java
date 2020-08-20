package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.UserCorporateComment;
import lombok.Data;

import java.util.Date;

@Data
public class UserCorporateCommentVo implements VoConvertable<UserCorporateCommentVo, UserCorporateComment> {
    private Long id; //笔记Id， Primary Key
    private Long jobApplyId; //职位Id
    private String comment; //用户笔记
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间

    @Override
    public UserCorporateCommentVo asVo(final UserCorporateComment userCorporateComment) {
        setId(userCorporateComment.getId());
        setJobApplyId(userCorporateComment.getJobApplyId());
        setComment(userCorporateComment.getComment());
        setCreateTime(userCorporateComment.getCreateTime());
        return this;
    }
}


