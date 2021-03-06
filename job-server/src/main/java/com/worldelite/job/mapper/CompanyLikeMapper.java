package com.worldelite.job.mapper;

import com.worldelite.job.entity.CompanyLike;

public interface CompanyLikeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_like
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_like
     *
     * @mbg.generated
     */
    int insert(CompanyLike record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_like
     *
     * @mbg.generated
     */
    int insertSelective(CompanyLike record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_like
     *
     * @mbg.generated
     */
    CompanyLike selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_like
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CompanyLike record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_company_like
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CompanyLike record);

    /**
     * 删除对象全部点赞信息
     * @param ownerId
     */
    void deleteByOwnerId(Long ownerId);

    /**
     * 根据fromId和ownerId查询
     * @param companyLike
     * @return
     */
    CompanyLike select(CompanyLike companyLike);

    /**
     * 根据fromId和ownerId删除
     * @param companyLike
     */
    void deleteSelective(CompanyLike companyLike);

    /**
     * 统计对象点赞数
     * @param ownerId
     * @return
     */
    Integer countByOwnerId(Long ownerId);
}