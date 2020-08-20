package com.worldelite.job.form;

import lombok.Data;

@Data
public class UserCorporateTagForm {
    private long id;
    private long userId;
    private long corporateId;
    private String tagName;
}
