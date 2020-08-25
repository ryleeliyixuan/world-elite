package com.worldelite.job.service;

import com.worldelite.job.entity.UserCorporateComment;
import com.worldelite.job.form.UserCorporateCommentForm;
import com.worldelite.job.mapper.UserCorporateCommentMapper;
import com.worldelite.job.vo.UserCorporateCommentVo;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
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

        UserCorporateComment userCorporateComment;
        if (userCorporateCommentForm.getId() == null || (userCorporateComment = userCorporateCommentMapper.selectByPrimaryKey(userCorporateCommentForm.getId())) == null) {
            userCorporateComment = new UserCorporateComment();
            userCorporateComment.setJobApplyId(userCorporateCommentForm.getJobApplyId());
            userCorporateComment.setComment(userCorporateCommentForm.getComment());
            userCorporateCommentMapper.insertSelective(userCorporateComment);
        } else {
            userCorporateComment.setUpdateTime(new Date());
            userCorporateComment.setComment(userCorporateCommentForm.getComment());
            userCorporateCommentMapper.updateByPrimaryKey(userCorporateComment);
        }
    }

    /**
     * 根据用户id取得用户笔记
     *
     * @param jobApplyId
     * @return
     */
    public List<UserCorporateCommentVo> getCommentsByJobApplyId(long jobApplyId) {

        final List<UserCorporateComment> userCorporateComments;
        userCorporateComments = userCorporateCommentMapper.selectByJobApplyId(jobApplyId);

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
