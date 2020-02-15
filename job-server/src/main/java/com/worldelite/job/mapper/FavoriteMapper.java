package com.worldelite.job.mapper;

import com.worldelite.job.entity.Favorite;

import java.util.List;

public interface FavoriteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_favorite
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_favorite
     *
     * @mbg.generated
     */
    int insert(Favorite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_favorite
     *
     * @mbg.generated
     */
    int insertSelective(Favorite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_favorite
     *
     * @mbg.generated
     */
    Favorite selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_favorite
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Favorite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_favorite
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Favorite record);

    List<Favorite> selectAndList(Favorite options);
}