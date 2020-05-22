package com.worldelite.job.vo;

import com.worldelite.job.entity.CompanyAddress;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyAddressVo implements VoConvertable<CompanyAddressVo, CompanyAddress>{

    private Integer id; //地址ID
    private Integer cityId; //城市ID
    private String address; //地址
    private Double latitude; //纬度
    private Double longitude; //经度

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
