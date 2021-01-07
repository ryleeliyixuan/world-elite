package com.worldelite.job.mapper;

import com.worldelite.job.entity.ResumeCertificate;

import java.util.List;

public interface ResumeCertificateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_certificate
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_certificate
     *
     * @mbg.generated
     */
    int insert(ResumeCertificate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_certificate
     *
     * @mbg.generated
     */
    int insertSelective(ResumeCertificate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_certificate
     *
     * @mbg.generated
     */
    ResumeCertificate selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_certificate
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(ResumeCertificate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_resume_certificate
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(ResumeCertificate record);

    List<ResumeCertificate>selectAndList(ResumeCertificate record);
}