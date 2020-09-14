package com.worldelite.job.mapper;

import com.worldelite.job.entity.AttachmentParser;

import java.util.List;

public interface AttachmentParserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_attachment_parser
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_attachment_parser
     *
     * @mbg.generated
     */
    int insert(AttachmentParser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_attachment_parser
     *
     * @mbg.generated
     */
    int insertSelective(AttachmentParser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_attachment_parser
     *
     * @mbg.generated
     */
    AttachmentParser selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_attachment_parser
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AttachmentParser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_attachment_parser
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(AttachmentParser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_attachment_parser
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AttachmentParser record);

    /**
     * 通过邮箱搜索解析结果
     * @param email
     * @return
     */
    List<AttachmentParser> selectByEmail(String email);
}