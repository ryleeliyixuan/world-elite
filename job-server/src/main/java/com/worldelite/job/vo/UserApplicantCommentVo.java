package com.worldelite.job.vo;

import com.worldelite.job.entity.UserApplicantComment;
import lombok.Data;

import java.util.Date;

@Data
public class UserApplicantCommentVo implements VoConvertable<UserApplicantCommentVo, UserApplicantComment> {
    private Long Id; //笔记Id， Primary Key
    private Long userId; //用户Id
    private Long companyId; //公司Id
    private String comment; //用户笔记
    private Date createTime; //创建时间

    @Override
    public UserApplicantCommentVo asVo(final UserApplicantComment userApplicantComment) {
        setId(userApplicantComment.getId());
        setUserId(userApplicantComment.getUserApplicantId());
        setCompanyId(companyId);
        setComment(userApplicantComment.getComment());
        setCreateTime(userApplicantComment.getCreateTime());
        return this;
    }
}


