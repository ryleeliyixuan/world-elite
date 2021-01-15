package com.worldelite.job.constants;

/**
 * @author Xiang Chao
 **/
public enum ActivityReviewType {
    INITIAL_REVIEW(1), //初次审核
    REVIEW_AFTER_EDITING(2); //编辑后审核

    public final byte value;

    ActivityReviewType(int value) {
        this.value = (byte)value;
    }
}
