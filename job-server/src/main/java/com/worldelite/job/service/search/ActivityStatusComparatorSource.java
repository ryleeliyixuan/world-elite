package com.worldelite.job.service.search;

import lombok.Data;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;

/**
 * 活动状态比较器源
 *
 * @author Xiang Chao
 **/
public class ActivityStatusComparatorSource extends FieldComparatorSource {

    @Override
    public FieldComparator<?> newComparator(String fieldName, int numHits, int sortPos, boolean reversed) {
        return new ActivityStatusComparator(numHits, fieldName, 0);
    }
}
