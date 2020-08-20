package com.worldelite.job.dto;

import com.worldelite.job.constants.BusinessType;
import com.worldelite.job.constants.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.document.Document;

/**
 * luence索引同步消息dto
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LuceneIndexCmdDto {
    private Document doc;
    private OperationType opType;
    private BusinessType bizType;
}
