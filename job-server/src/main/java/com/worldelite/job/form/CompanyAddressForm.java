package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyAddressForm {
    private Integer id;
    private Long companyId;
    private String city;
    @NotBlank(message = "{edit.company.address.not.blank}")
    private String address;
    @NotNull(message = "{edit.company.location.not.null}")
    private Double latitude;
    @NotNull(message = "{edit.company.location.not.null}")
    private Double longitude;
}
