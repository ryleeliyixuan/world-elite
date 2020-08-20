package com.worldelite.job.vo;

import com.worldelite.job.entity.UserCorporateComment;
import lombok.Data;

import java.util.Date;

@Data
public class UserCorporateCommentVo implements VoConvertable<UserCorporateCommentVo, UserCorporateComment> {
    private Long Id; //笔记Id， Primary Key
    private Long userId; //用户Id
    private Long companyId; //公司Id
    private String comment; //用户笔记
    private Date createTime; //创建时间

    @Override
    public UserCorporateCommentVo asVo(final UserCorporateComment userCorporateComment) {
        setId(userCorporateComment.getId());
        setUserId(userCorporateComment.getUserId());
        setCompanyId(userCorporateComment.getCompanyId());
        setComment(userCorporateComment.getComment());
        setCreateTime(userCorporateComment.getCreateTime());
        return this;
    }
}


