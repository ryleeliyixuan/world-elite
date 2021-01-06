package com.worldelite.job.mapper;

import com.worldelite.job.entity.Registration;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegistrationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_registration
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_registration
     *
     * @mbg.generated
     */
    int insert(Registration record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_registration
     *
     * @mbg.generated
     */
    int insertSelective(Registration record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_registration
     *
     * @mbg.generated
     */
    Registration selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_registration
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Registration record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_registration
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Registration record);

    List<Registration> selectAndList(Registration registration);

    Registration selectRegistrationStatusByUserId(@Param("activityId") Integer activityId, @Param("registrationUserId") Long registrationUserId);
}