package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyAddressForm {
    private Integer id; //地址ID
    private Long companyId; //公司ID
    private String city; //城市
    @NotBlank(message = "{edit.company.address.not.blank}")
    private String address; //地址
    @NotNull(message = "{edit.company.location.not.null}")
    private Double latitude; //纬度
    @NotNull(message = "{edit.company.location.not.null}")
    private Double longitude; //经度
}
