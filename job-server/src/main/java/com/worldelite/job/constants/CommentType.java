package com.worldelite.job.constants;

/**
 * 评论类型
 * @author 邓集阶
 */
public enum CommentType {

    POST(1), //评论帖子
    SCORE(2), //评论评分
    COMMENT(3); //评论评论
    public final byte value;

    CommentType(int value) {
        this.value = (byte)value;
    }

    public static CommentType valueOf(byte value){
        if(value == POST.value){
            return POST;
        }else if(value == SCORE.value){
            return SCORE;
        }else if(value == COMMENT.value){
            return COMMENT;
        }
        return null;
    }
}
