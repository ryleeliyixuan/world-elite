package com.worldelite.job.mapper;

import com.worldelite.job.entity.SearchJobHistory;
import com.worldelite.job.entity.SearchJobHistoryOptions;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SearchJobHistoryMapper继承基类
 */
@Mapper
@Repository
public interface SearchJobHistoryMapper extends MyBatisBaseDao<SearchJobHistory, Long> {
    List<SearchJobHistory> selectAndList(SearchJobHistoryOptions searchJobHistoryOptions);
}