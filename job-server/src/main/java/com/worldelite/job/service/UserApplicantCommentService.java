package com.worldelite.job.service;

import com.worldelite.job.entity.UserApplicantComment;
import com.worldelite.job.form.UserApplicantCommentForm;
import com.worldelite.job.mapper.UserApplicantCommentMapper;
import com.worldelite.job.vo.UserApplicantCommentVo;
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
public class UserApplicantCommentService {

    @Autowired
    private UserApplicantCommentMapper userApplicantCommentMapper;

    /**
     * 写入新的用户笔记
     * @param userApplicantCommentForm
     * @return
     */
    public void saveComment(final @NonNull UserApplicantCommentForm userApplicantCommentForm) {
        final UserApplicantComment userApplicantComment = new UserApplicantComment();

        if (StringUtils.isNotEmpty(userApplicantCommentForm.getComment())) {
            userApplicantComment.setComment(userApplicantCommentForm.getComment());
        }

        userApplicantComment.setUserApplicantId(userApplicantCommentForm.getUserApplicantId());
        userApplicantComment.setCompanyId(userApplicantCommentForm.getCompanyId());

        userApplicantCommentMapper.insertSelective(userApplicantComment);
    }

    /**
     * 根据用户id取得用户笔记
     * @param userId
     * @return
     */
    public List<UserApplicantCommentVo> getCommentsByUserIdAndCompanyId(
            final @NonNull UserApplicantCommentForm userApplicantCommentForm) {

        final List<UserApplicantComment> userApplicantComments;
        final long companyId = userApplicantCommentForm.getCompanyId();
        final long userApplicantId = userApplicantCommentForm.getUserApplicantId();

        if (Objects.nonNull(companyId) && Objects.nonNull(userApplicantId)) {
            userApplicantComments  = userApplicantCommentMapper.selectByUserIdAndCompanyId(userApplicantId, companyId);

            if (Objects.nonNull(userApplicantComments)) {
                return userApplicantComments.stream()
                        .map(userApplicantComment -> new UserApplicantCommentVo().asVo(userApplicantComment))
                        .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    /**
     * 删除用户笔记
     * @param userApplicantCommentId
     * @return
     */
    public void deleteCommentById(final @NonNull Long userApplicantCommentId) {
        userApplicantCommentMapper.deleteByPrimaryKey(userApplicantCommentId);
    }
}
