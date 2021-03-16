package com.worldelite.job.dto;

import com.worldelite.job.constants.BusinessType;
import com.worldelite.job.constants.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.document.Document;

/**
 * Lucene索引同步消息dto
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LuceneIndexCmdDto {
    /**
     * 关键key. id或其他
     */
    private Object key;
    /**
     * 操作类型. Insert/Delete/Update
     */
    private OperationType opType;
    /**
     * 业务类型. 职位/活动/简历
     */
    private BusinessType bizType;
}
