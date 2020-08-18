package com.worldelite.job.service;

import com.worldelite.job.entity.UserCorporateTag;
import com.worldelite.job.form.UserCorporateTagForm;
import com.worldelite.job.mapper.UserCorporateTagMapper;
import com.worldelite.job.vo.UserCorporateTagVo;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        final UserCorporateTag userCorporateTag = new UserCorporateTag();

        if(StringUtils.isNotEmpty(userCorporateTagForm.getTagName())) {
            userCorporateTag.setTagName(userCorporateTagForm.getTagName());
        }
        userCorporateTag.setId(userCorporateTagForm.getId());
        userCorporateTag.setUserId(userCorporateTagForm.getUserId());
        userCorporateTag.setCorporateId(userCorporateTagForm.getCorporateId());
        userCorporateTag.setTagName(userCorporateTagForm.getTagName());

        userCorporateTagMapper.insertSelective(userCorporateTag);
    }

    /**
     * 根据企业id和用户id取得标签
     * @param userId,corporateId
     * @return
     */
    public List<UserCorporateTagVo> getTagsByUserIdCorporateId(final @NonNull Long userId,final @NonNull Long corporateId) {
        final List<UserCorporateTag> userCorporateTags = userCorporateTagMapper.selectByUserCorporateId(userId,corporateId);

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
        UserCorporateTag tag = userCorporateTagMapper.selectByPrimaryKey(id);
        if(tag != null){
            userCorporateTagMapper.deleteByPrimaryKey(id);
        }
    }
}
