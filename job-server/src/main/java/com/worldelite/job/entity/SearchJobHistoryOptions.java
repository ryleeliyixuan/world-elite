package com.worldelite.job.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @Author twz
 * @Date 2021-01-14
 * @Desc TODO
 */
@Data
@Builder
public class SearchJobHistoryOptions {
    private Long userId;
    private Long historyId;
}
