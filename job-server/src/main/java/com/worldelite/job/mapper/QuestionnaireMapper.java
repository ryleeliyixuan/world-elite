package com.worldelite.job.mapper;

import com.worldelite.job.entity.Questionnaire;

import java.util.List;

public interface QuestionnaireMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire
     *
     * @mbg.generated
     */
    int insert(Questionnaire record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire
     *
     * @mbg.generated
     */
    int insertSelective(Questionnaire record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire
     *
     * @mbg.generated
     */
    Questionnaire selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Questionnaire record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Questionnaire record);

    List<Questionnaire> selectAndList(Questionnaire questionnaire);

    void deleteByTemplateId(Integer templateId);
}