package com.worldelite.job.mapper;

import com.worldelite.job.entity.SearchJobHistoryInfo;
import com.worldelite.job.entity.SearchJobHistoryOptions;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SearchJobHistoryInfoMapper继承基类
 */
@Mapper
@Repository
public interface SearchJobHistoryInfoMapper extends MyBatisBaseDao<SearchJobHistoryInfo, Long> {

    List<SearchJobHistoryInfo> selectAndList(SearchJobHistoryOptions searchJobHistoryOptions);
}