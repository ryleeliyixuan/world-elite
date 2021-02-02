package com.worldelite.job.service.search;

import org.apache.lucene.search.FieldComparator;

/**
 * 自定义活动状态比较器
 * 报名中(4)＞即将开始(3)＞进行中(5)＞已结束(6)
 *
 * @author Xiang Chao
 **/
public class ActivityStatusComparator extends FieldComparator.NumericComparator<Integer> {
    private final int[] values;
    private int bottom;                           // Value of bottom of queue
    private int topValue;

    /**
     * Creates a new comparator based on {@link Integer#compare} for {@code numHits}.
     * When a document has no value for the field, {@code missingValue} is substituted.
     */
    public ActivityStatusComparator(int numHits, String field, Integer missingValue) {
        super(field, missingValue);
        values = new int[numHits];
    }

    @Override
    public int compare(int slot1, int slot2) {
        int p1, p2;
        p1 = values[slot1] == 4 ? 0 : values[slot1];
        p2 = values[slot2] == 4 ? 0 : values[slot2];

        return Integer.compare(p1, p2);
    }

    @Override
    public int compareBottom(int doc) {
        int v2 = (int) currentReaderValues.get(doc);
        // Test for v2 == 0 to save Bits.get method call for
        // the common case (doc has value and value is non-zero):
        if (docsWithField != null && v2 == 0 && !docsWithField.get(doc)) {
            v2 = missingValue;
        }

        if (v2 == 4) v2 = 0;

        return Integer.compare(bottom, v2);
    }

    @Override
    public void copy(int slot, int doc) {
        int v2 = (int) currentReaderValues.get(doc);
        // Test for v2 == 0 to save Bits.get method call for
        // the common case (doc has value and value is non-zero):
        if (docsWithField != null && v2 == 0 && !docsWithField.get(doc)) {
            v2 = missingValue;
        }

        values[slot] = v2;
    }

    @Override
    public void setBottom(final int bottom) {
        this.bottom = values[bottom];
    }

    @Override
    public void setTopValue(Integer value) {
        topValue = value;
    }

    @Override
    public Integer value(int slot) {
        return values[slot];
    }

    @Override
    public int compareTop(int doc) {
        int docValue = (int) currentReaderValues.get(doc);
        // Test for docValue == 0 to save Bits.get method call for
        // the common case (doc has value and value is non-zero):
        if (docsWithField != null && docValue == 0 && !docsWithField.get(doc)) {
            docValue = missingValue;
        }

        if (docValue == 4) docValue = 0;

        return Integer.compare(topValue, docValue);
    }
}
