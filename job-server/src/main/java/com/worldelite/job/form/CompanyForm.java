package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyForm {
    private Long id;
    private String name;
    private String logo;
    private Integer scaleId;
    private Integer stageId;
    private Integer industryId;
    private Integer propertyId;
    private String homepage;
    private String introduction;
}
