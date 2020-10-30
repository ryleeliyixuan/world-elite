package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.UserCorporateTag;
import lombok.Data;

import java.util.Date;

@Data
public class UserCorporateTagVo implements VoConvertable<UserCorporateTagVo,UserCorporateTag> {
    private String id; //标签ID
    private String resumeId; //简历ID
    private String tagName; //标签
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //标签创建时间

    @Override
    public UserCorporateTagVo asVo(final UserCorporateTag userCorporateTag) {
        setId(String.valueOf(userCorporateTag.getId()));
        setResumeId(String.valueOf(userCorporateTag.getResumeId()));
        setTagName(userCorporateTag.getTagName());
        setCreateTime(userCorporateTag.getCreateTime());
        return this;
    }
}
