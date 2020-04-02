package com.worldelite.job.vo;

import com.worldelite.job.entity.Country;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CountryVo implements VoConvertable<CountryVo, Country>{

    private Integer id;
    private String phoneCode;
    private String countryCode;
    private String chineseName;
    private String englishName;

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
