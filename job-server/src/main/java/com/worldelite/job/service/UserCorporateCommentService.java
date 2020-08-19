package com.worldelite.job.service;

import com.worldelite.job.entity.UserCorporateComment;
import com.worldelite.job.form.UserCorporateCommentForm;
import com.worldelite.job.mapper.UserCorporateCommentMapper;
import com.worldelite.job.vo.UserCorporateCommentVo;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 王星瀚
 */
@Service
public class UserCorporateCommentService {

    @Autowired
    private UserCorporateCommentMapper userCorporateCommentMapper;

    /**
     * 写入新的用户笔记
     * @param userCorporateCommentForm
     * @return
     */
    public void saveComment(final @NonNull UserCorporateCommentForm userCorporateCommentForm) {
        final UserCorporateComment userCorporateComment = new UserCorporateComment();

        if (StringUtils.isNotEmpty(userCorporateCommentForm.getComment())) {
            userCorporateComment.setComment(userCorporateCommentForm.getComment());
        }

        userCorporateComment.setUserId(userCorporateCommentForm.getUserId());
        userCorporateComment.setCompanyId(userCorporateCommentForm.getCompanyId());

        userCorporateCommentMapper.insertSelective(userCorporateComment);
    }

    /**
     * 根据用户id取得用户笔记
     *
     * @param userId
     * @return
     */
    public List<UserCorporateCommentVo> getCommentsByUserIdAndCompanyId(long userId, long corporateId) {

        final List<UserCorporateComment> userCorporateComments;
        userCorporateComments = userCorporateCommentMapper.selectByUserIdAndCompanyId(userId, corporateId);

        if (Objects.nonNull(userCorporateComments)) {
            return userCorporateComments.stream()
                    .map(userCorporateComment -> new UserCorporateCommentVo().asVo(userCorporateComment))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * 删除用户笔记
     * @param userCorporateCommentId
     * @return
     */
    public void deleteCommentById(final @NonNull Long userCorporateCommentId) {
        userCorporateCommentMapper.deleteByPrimaryKey(userCorporateCommentId);
    }
}
