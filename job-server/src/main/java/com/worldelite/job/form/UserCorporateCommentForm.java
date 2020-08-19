package com.worldelite.job.form;

import lombok.Data;

@Data
public class UserCorporateCommentForm {
    private long userId;
    private long companyId;
    private String comment;
}
