package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanyAddress;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyAddressVo implements VoConvertable<CompanyAddressVo, CompanyAddress>{

    private Integer id;
    private Integer cityId;
    private String address;
    private Double latitude;
    private Double longitude;

    @Override
    public CompanyAddressVo asVo(CompanyAddress companyAddress) {
        setId(companyAddress.getId());
        setCityId(companyAddress.getCityId());
        setAddress(companyAddress.getAddress());
        setLatitude(companyAddress.getLatitude());
        setLongitude(companyAddress.getLongitude());
        return this;
    }
}
