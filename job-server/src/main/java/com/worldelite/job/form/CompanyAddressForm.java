package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyAddressForm {
    private Integer id;
    private Long companyId;
    private String city;
    private String address;
    private Double latitude;
    private Double longitude;
}
