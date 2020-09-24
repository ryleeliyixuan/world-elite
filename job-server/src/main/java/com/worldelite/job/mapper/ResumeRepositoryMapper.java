package com.worldelite.job.mapper;

import com.worldelite.job.entity.ResumeRepository;

import java.util.List;

public interface ResumeRepositoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_repository
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_repository
     *
     * @mbg.generated
     */
    int insert(ResumeRepository record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_repository
     *
     * @mbg.generated
     */
    int insertSelective(ResumeRepository record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_repository
     *
     * @mbg.generated
     */
    ResumeRepository selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_repository
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ResumeRepository record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_repository
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ResumeRepository record);

    ResumeRepository selectByResumeId(Long resumeId);


    ResumeRepository selectByUserId(Long userId);


    List<ResumeRepository> selectByCompanyId(Long companyId);


    List<ResumeRepository> selectByCreatorId(Long creatorId);
}