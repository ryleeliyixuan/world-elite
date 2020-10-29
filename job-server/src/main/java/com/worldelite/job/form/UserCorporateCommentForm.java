package com.worldelite.job.form;

import lombok.Data;

@Data
public class UserCorporateCommentForm {
    private Long id;
    private Long resumeId;
    private String comment;
}
