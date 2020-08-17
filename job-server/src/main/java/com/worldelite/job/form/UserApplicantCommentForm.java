package com.worldelite.job.form;

import lombok.Data;

@Data
public class UserApplicantCommentForm {
    private long userApplicantId;
    private long companyId;
    private String comment;
}
