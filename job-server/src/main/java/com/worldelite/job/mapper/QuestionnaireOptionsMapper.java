package com.worldelite.job.mapper;

import com.worldelite.job.entity.QuestionnaireOptions;

import java.util.List;

public interface QuestionnaireOptionsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire_options
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire_options
     *
     * @mbg.generated
     */
    int insert(QuestionnaireOptions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire_options
     *
     * @mbg.generated
     */
    int insertSelective(QuestionnaireOptions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire_options
     *
     * @mbg.generated
     */
    QuestionnaireOptions selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire_options
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(QuestionnaireOptions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_questionnaire_options
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(QuestionnaireOptions record);

    List<QuestionnaireOptions> selectAndList(QuestionnaireOptions questionnaireOptions);
}