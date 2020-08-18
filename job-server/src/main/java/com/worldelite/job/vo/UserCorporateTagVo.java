package com.worldelite.job.vo;

import com.worldelite.job.entity.UserCorporateTag;
import lombok.Data;

import java.util.Date;

@Data
public class UserCorporateTagVo implements VoConvertable<UserCorporateTagVo,UserCorporateTag> {
    private Long id; //标签ID
    private Long userId; //个人用户ID
    private Long corporateId; //企业用户ID
    private String tagName; //企业给个人用户设置的标签
    private Date createTime; //标签创建时间

    @Override
    public UserCorporateTagVo asVo(final UserCorporateTag userCorporateTag) {
        setId(userCorporateTag.getId());
        setUserId(userCorporateTag.getUserId());
        setCorporateId(userCorporateTag.getCorporateId());
        setTagName(userCorporateTag.getTagName());
        setCreateTime(userCorporateTag.getCreateTime());
        return this;
    }
}
