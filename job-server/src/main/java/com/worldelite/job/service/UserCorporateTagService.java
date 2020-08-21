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
public class UserCorporateTagService {
    @Autowired
    UserCorporateTagMapper userCorporateTagMapper;

    /**
     * 写入新的用户标签
     * @param userCorporateTagForm
     * @return
     */
    public void saveTag(final @NonNull UserCorporateTagForm userCorporateTagForm) {

        UserCorporateTag userCorporateTag;
        if (userCorporateTagForm.getId() == null || (userCorporateTag = userCorporateTagMapper.selectByPrimaryKey(userCorporateTagForm.getId())) == null) {
            userCorporateTag = new UserCorporateTag();
            userCorporateTag.setJobApplyId(userCorporateTagForm.getJobApplyId());
            userCorporateTag.setTagName(userCorporateTagForm.getTagName());
            userCorporateTagMapper.insertSelective(userCorporateTag);
        } else {
            userCorporateTag.setTagName(userCorporateTagForm.getTagName());
            userCorporateTag.setUpdateTime(new Date());
            userCorporateTagMapper.updateByPrimaryKey(userCorporateTag);
        }
    }

    /**
     * 职位申请ID
     *
     * @param jobApplyId
     * @return
     */
    public List<UserCorporateTagVo> getTagsByJobApplyId(final @NonNull long jobApplyId) {
        final List<UserCorporateTag> userCorporateTags = userCorporateTagMapper.selectByJobApplyId(jobApplyId);

        if (Objects.nonNull(userCorporateTags)) {
            return userCorporateTags.stream()
                    .map(userCorporateTag -> new UserCorporateTagVo().asVo(userCorporateTag))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * 删除标签
     *
     * @param id
     */
    public void delTag(Long id){
        userCorporateTagMapper.deleteByPrimaryKey(id);
    }
}
