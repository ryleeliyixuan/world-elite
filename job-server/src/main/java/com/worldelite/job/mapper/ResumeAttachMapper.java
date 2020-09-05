package com.worldelite.job.mapper;

import com.worldelite.job.entity.ResumeAttach;

import java.util.List;

public interface ResumeAttachMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_attach
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_attach
     *
     * @mbg.generated
     */
    int insert(ResumeAttach record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_attach
     *
     * @mbg.generated
     */
    int insertSelective(ResumeAttach record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_attach
     *
     * @mbg.generated
     */
    ResumeAttach selectByPrimaryKey(Integer id);

    ResumeAttach selectByResumeId(Long resumeId);

    ResumeAttach selectByResumeIdWithBLOBs(Long resumeId);

    List<ResumeAttach> listAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_attach
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ResumeAttach record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_attach
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(ResumeAttach record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_attach
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ResumeAttach record);
}