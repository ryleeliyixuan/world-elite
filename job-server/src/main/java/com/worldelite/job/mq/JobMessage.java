package com.worldelite.job.mq;

import com.worldelite.job.vo.JobVo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
@AllArgsConstructor
public class JobMessage {
    private JobVo job;
}
