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
public class UserCorporateCommentService extends BaseService{

    @Autowired
    private UserCorporateCommentMapper userCorporateCommentMapper;

    /**
     * 写入新的用户笔记
     * @param userCorporateCommentForm
     * @return
     */
    public UserCorporateCommentVo saveComment(final @NonNull UserCorporateCommentForm userCorporateCommentForm) {

        UserCorporateComment userCorporateComment;
        if (userCorporateCommentForm.getId() == null || (userCorporateComment = userCorporateCommentMapper.selectByPrimaryKey(userCorporateCommentForm.getId())) == null) {
            userCorporateComment = new UserCorporateComment();
            userCorporateComment.setCreatorId(curUser().getId());
            userCorporateComment.setResumeId(userCorporateCommentForm.getResumeId());
            userCorporateComment.setComment(userCorporateCommentForm.getComment());
            userCorporateCommentMapper.insertSelective(userCorporateComment);
        } else {
            userCorporateComment.setUpdateTime(new Date());
            userCorporateComment.setComment(userCorporateCommentForm.getComment());
            userCorporateCommentMapper.updateByPrimaryKey(userCorporateComment);
        }
        return new UserCorporateCommentVo().asVo(userCorporateComment);
    }

    /**
     * 根据简历id取得笔记
     *
     * @param resumeId
     * @return
     */
    public List<UserCorporateCommentVo> getCommentsByResumeId(long resumeId) {
        UserCorporateComment corporateComment = new UserCorporateComment();
        corporateComment.setResumeId(resumeId);
        corporateComment.setCreatorId(curUser().getId());
        List<UserCorporateComment> userCorporateCommentList = userCorporateCommentMapper.selectAndList(corporateComment);
        if (Objects.nonNull(userCorporateCommentList)) {
            return userCorporateCommentList.stream()
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
    public void deleteCommentById(long userCorporateCommentId) {
        userCorporateCommentMapper.deleteByPrimaryKey(userCorporateCommentId);
    }
}
