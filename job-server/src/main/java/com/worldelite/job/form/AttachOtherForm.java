package com.worldelite.job.form;

import lombok.Data;

/**
 * Nuo Xu
 */
@Data
public class AttachOtherForm {
    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    private String link;
    private String name;
}
