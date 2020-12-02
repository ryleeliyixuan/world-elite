package com.worldelite.job.mapper;

import com.worldelite.job.entity.User;
import com.worldelite.job.entity.UserApplicant;
import com.worldelite.job.entity.UserOptions;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserApplicantMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_applicant
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_applicant
     *
     * @mbg.generated
     */
    int insert(UserApplicant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_applicant
     *
     * @mbg.generated
     */
    int insertSelective(UserApplicant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_applicant
     *
     * @mbg.generated
     */
    UserApplicant selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_applicant
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserApplicant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_applicant
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserApplicant record);

    UserApplicant selectByEmail(String email);

    List<UserApplicant> selectAndList(UserOptions options);

    List<Map<String, Object>> selectStatInfo(Integer days);

    List<Map<String, Object>> selectSpecifyMonthStatInfo(String date);

    Date selectFirstCreateTime();

    List<Map<String, Object>> selectSpecifyYearStatInfo(String dateStr);
}