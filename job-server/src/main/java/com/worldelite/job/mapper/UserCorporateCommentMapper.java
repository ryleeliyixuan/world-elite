package com.worldelite.job.mapper;

import com.worldelite.job.entity.UserCorporateComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCorporateCommentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_corporate_comment
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_corporate_comment
     *
     * @mbg.generated
     */
    int insert(UserCorporateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_corporate_comment
     *
     * @mbg.generated
     */
    int insertSelective(UserCorporateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_corporate_comment
     *
     * @mbg.generated
     */
    UserCorporateComment selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_corporate_comment
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserCorporateComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_corporate_comment
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserCorporateComment record);

    List<UserCorporateComment> selectByJobApplyId(@Param("jobApplyId") Long jobApplyId);
}