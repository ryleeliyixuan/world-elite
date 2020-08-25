package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.UserCorporateTag;
import lombok.Data;

import java.util.Date;

@Data
public class UserCorporateTagVo implements VoConvertable<UserCorporateTagVo,UserCorporateTag> {
    private Long id; //标签ID
    private Long jobApplyId; //职位申请ID
    private String tagName; //企业给个人用户设置的标签
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //标签创建时间

    @Override
    public UserCorporateTagVo asVo(final UserCorporateTag userCorporateTag) {
        setId(userCorporateTag.getId());
        setJobApplyId(userCorporateTag.getJobApplyId());
        setTagName(userCorporateTag.getTagName());
        setCreateTime(userCorporateTag.getCreateTime());
        return this;
    }
}
