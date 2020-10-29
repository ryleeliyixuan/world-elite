package com.worldelite.job.service;

import com.worldelite.job.entity.UserCorporateTag;
import com.worldelite.job.form.UserCorporateTagForm;
import com.worldelite.job.mapper.UserCorporateTagMapper;
import com.worldelite.job.vo.UserCorporateTagVo;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserCorporateTagService extends BaseService{
    @Autowired
    UserCorporateTagMapper userCorporateTagMapper;

    /**
     * 写入新的用户标签
     * @param userCorporateTagForm
     * @return
     */
    public UserCorporateTagVo saveTag(UserCorporateTagForm userCorporateTagForm) {
        UserCorporateTag userCorporateTag;
        if (userCorporateTagForm.getId() == null || (userCorporateTag = userCorporateTagMapper.selectByPrimaryKey(userCorporateTagForm.getId())) == null) {
            userCorporateTag = new UserCorporateTag();
            userCorporateTag.setResumeId(userCorporateTagForm.getResumeId());
            userCorporateTag.setCreatorId(curUser().getId());
            userCorporateTag.setTagName(userCorporateTagForm.getTagName());
            userCorporateTagMapper.insertSelective(userCorporateTag);
        } else {
            userCorporateTag.setTagName(userCorporateTagForm.getTagName());
            userCorporateTag.setUpdateTime(new Date());
            userCorporateTagMapper.updateByPrimaryKey(userCorporateTag);
        }
        return new UserCorporateTagVo().asVo(userCorporateTag);
    }

    /**
     * 获取简历标签
     *
     * @param resumeId
     * @return
     */
    public List<UserCorporateTagVo> getTagsByResumeId(long resumeId) {
        UserCorporateTag userCorporateTag = new UserCorporateTag();
        userCorporateTag.setCreatorId(curUser().getId());
        userCorporateTag.setResumeId(resumeId);
        List<UserCorporateTag> corporateTagList = userCorporateTagMapper.selectAndList(userCorporateTag);
        if (Objects.nonNull(corporateTagList)) {
            return corporateTagList.stream()
                    .map(corporateTag -> new UserCorporateTagVo().asVo(corporateTag))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 删除标签
     *
     * @param id
     */
    public void delTag(long id){
        userCorporateTagMapper.deleteByPrimaryKey(id);
    }
}
