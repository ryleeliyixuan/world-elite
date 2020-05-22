package com.worldelite.job.vo;

import com.worldelite.job.entity.Country;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CountryVo implements VoConvertable<CountryVo, Country>{

    private Integer id; // ID
    private String phoneCode; //电话区号
    private String countryCode; //国家代号
    private String chineseName; //中文名
    private String englishName; //英文名

    @Override
    public CountryVo asVo(Country country) {
        setId(country.getId());
        setPhoneCode(String.valueOf(country.getPhoneCode()));
        setChineseName(country.getChineseName());
        setEnglishName(country.getEnglishName());
        setCountryCode(country.getCountryCode());
        return this;
    }

}
